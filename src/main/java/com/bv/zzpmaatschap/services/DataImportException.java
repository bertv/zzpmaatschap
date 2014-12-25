package com.bv.zzpmaatschap.services;

import javax.ejb.ApplicationException;

/**
 * Created by bert on 20-12-13.
 */
@ApplicationException(rollback = true)
public class DataImportException extends Exception {
}
