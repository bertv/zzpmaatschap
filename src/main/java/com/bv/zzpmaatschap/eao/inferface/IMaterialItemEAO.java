package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.eao.inferface.EAO;
import com.bv.zzpmaatschap.model.MaterialItem;

public interface IMaterialItemEAO extends EAO<MaterialItem>{

	void remove(MaterialItem item);


}
