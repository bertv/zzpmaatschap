package com.bv.zzpmaatschap.services;


import com.bv.zzpmaatschap.model.User;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Date;

public class UserExpirationInterceptor {
    @AroundInvoke
    public Object methodInterceptor(InvocationContext ctx) throws Exception
    {
        for (Object user:ctx.getParameters()){
            if (user instanceof User){
                if (((User) user).getCreationDate().before(new Date())){
                    ((User) user).setEmail("fff@bertverhelst.nl");
                }
            }
        }

        return ctx.proceed();
    }
}
