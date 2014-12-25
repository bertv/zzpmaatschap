package com.bv.zzpmaatschap.services.interfaces;


import com.bv.zzpmaatschap.model.User;
import com.bv.zzpmaatschap.services.ForgetMessage;

import javax.ejb.Remote;
import java.security.Principal;
import java.util.List;
@Remote
public interface IUserService {

    User merge(User user);

    Iterable<? extends String> getRoles();

    ForgetMessage forgotPassword(String email);

    User getCurrentUser(Principal userPrincipal);
    User getCurrentUser(String userPrincipal);
    void register(User user);

    List<User> getAllUsers();
}
