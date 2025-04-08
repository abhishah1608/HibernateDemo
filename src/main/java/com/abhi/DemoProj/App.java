package com.abhi.DemoProj;
import com.abhi.DbOperation.InsertUser;

import Utils.HibernateUtil;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        InsertUser u = new InsertUser();
        // u.GetData();
        // u.updateUser();
        u.demoCacheL2();
        HibernateUtil.shutdown(); // Close SessionFactory on app exit (optional)

    }
    
}
