package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.Role;

import java.util.List;

public interface IRoleEAO extends EAO<Role> {


    List<String> getRoles();
}
