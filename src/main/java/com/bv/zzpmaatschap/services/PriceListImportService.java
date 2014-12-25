package com.bv.zzpmaatschap.services;

import com.bv.zzpmaatschap.eao.inferface.IPriceListEAO;
import com.bv.zzpmaatschap.eao.inferface.IPriceListItemEAO;
import com.bv.zzpmaatschap.model.pricelist.PriceList;
import com.bv.zzpmaatschap.model.pricelist.PriceListItem;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import javax.ejb.*;
import java.io.*;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Stateless
public class PriceListImportService {

    @EJB(beanName = "priceListEAO",beanInterface = IPriceListEAO.class)
    IPriceListEAO priceListEAO;
    @EJB(beanName = "priceListItemEAO",beanInterface = IPriceListItemEAO.class)
    IPriceListItemEAO priceListItemService;

private Future<String> future;

    public Future<String> getFuture() {

        return future;
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public PriceList createPricelist(String priceListName){
        PriceList priceList = new PriceList();
        priceList.setName(priceListName);
        priceListEAO.persist(priceList);
        return priceList;
    }

    public void importFile(File file, PriceList priceList) throws DataImportException, FileNotFoundException {
       importFile(new FileInputStream(file),priceList);
    }
    public void countItems(InputStream inputStream, PriceList priceList) throws DataImportException {
        ICsvBeanReader beanReader =null;
        try {




            // the header elements are used to map the values to the bean (names must match)
            final String[] header = new String[]{"itemNumber", "name", "amount", "unit", "price"};
            final CellProcessor[] processors = getProcessors();
            long itemCount=0;
            beanReader=  new CsvBeanReader(new InputStreamReader(inputStream), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            while (( beanReader.read(PriceListItem.class, header, processors)) != null) {


                itemCount++;

            }
            priceList.setTotal(itemCount);
            priceListEAO.merge(priceList);

        } catch (final org.supercsv.exception.SuperCsvCellProcessorException sce) {


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException e) {

                }
            }
        }

    }

    @Asynchronous
    public void importFile(InputStream inputStream, PriceList priceList) throws DataImportException {
        ICsvBeanReader beanReader =null;
        try {




            // the header elements are used to map the values to the bean (names must match)
            final String[] header = new String[]{"itemNumber", "name", "amount", "unit", "price"};
            final CellProcessor[] processors = getProcessors();

            beanReader=new CsvBeanReader(new InputStreamReader(inputStream), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
             transactionalProcess(priceList, beanReader, header, processors);

        } catch (final org.supercsv.exception.SuperCsvCellProcessorException sce) {


        } catch (Exception e) {
            throw new DataImportException();
        } finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    throw new DataImportException();
                }
            }

        }

    }

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void transactionalProcess(PriceList priceList, ICsvBeanReader beanReader, String[] header, CellProcessor[] processors) throws DataImportException {


        PriceListItem item;


        try {

            while ((item = beanReader.read(PriceListItem.class, header, processors)) != null) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
                        beanReader.getRowNumber(), item));

                persistItem(priceList, item);

            }




        } catch (IOException e) {
            throw new DataImportException();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void persistItem(PriceList priceList, PriceListItem item) {
        item.setPriceList(priceList);
        try{
        priceListItemService.persist(item);
        }catch(Exception e){
            failedCounter(priceList);
        }
    }

    public void failedCounter(PriceList priceList) {
        priceList.setFailed(priceList.getFailed()+1);
        priceListEAO.merge(priceList);
    }

    private static CellProcessor[] getProcessors() {

        DecimalFormatSymbols FRENCH_SYMBOLS = new DecimalFormatSymbols(Locale.FRANCE);

        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(), // customerNo (must be unique)
                new NotNull(), // firstName

                new NotNull(new ParseInt()), // numberOfKids
                new NotNull(), // favouriteQuote
                new NotNull(new ParseBigDecimal(FRENCH_SYMBOLS)),

        };

        return processors;
    }


}
