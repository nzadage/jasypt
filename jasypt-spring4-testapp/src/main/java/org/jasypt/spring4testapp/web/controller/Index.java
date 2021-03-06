/*
 * =============================================================================
 * 
 *   Copyright (c) 2007-2010, The JASYPT team (http://www.jasypt.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.jasypt.spring4testapp.web.controller;

import java.util.Properties;

import org.jasypt.digest.ByteDigester;
import org.jasypt.digest.StringDigester;
import org.jasypt.encryption.pbe.PBEBigDecimalEncryptor;
import org.jasypt.encryption.pbe.PBEBigIntegerEncryptor;
import org.jasypt.encryption.pbe.PBEByteEncryptor;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class Index {

    @Autowired
    private PBEByteEncryptor enc1;

    @Autowired
    private PBEBigDecimalEncryptor enc3;

    @Autowired
    private PBEBigIntegerEncryptor enc4;

    @Autowired
    private PBEStringEncryptor encAES;

    @Autowired
    private BasicTextEncryptor bte;

    @Autowired
    private StrongTextEncryptor ste;
    
    @Autowired
    private StringDigester sd;
    
    @Autowired
    private ByteDigester bd;
    
    @Autowired
    private PasswordEncryptor bpe;

    
    @Autowired
    @Qualifier("eprop1")
    private Properties eprop1;
    
    @Autowired
    private ApplicationContext appCtx;

    
    @Autowired
    @Qualifier("eprop2")
    private Properties eprop2;


    @Value("${city.name}")
    private String cityName;
    
    @Value("${country.name}")
    private String countryName;
    
    
    public Index() {
        super();
    }
    
    @RequestMapping({"/"})
    public String show(final ModelMap model) {
        
        final byte[] mba = "Hello!".getBytes();
        final byte[] encMba = this.enc1.encrypt(mba);
        model.addAttribute("encMba", new String(encMba) + " | " + this.enc1.getClass().getName());
        
        final StandardPBEStringEncryptor enctest = new StandardPBEStringEncryptor();
        enctest.setPassword("jasypt");
        enctest.setAlgorithm("PBEWithMD5AndTripleDES");
        enctest.setStringOutputType("hexa");
        enctest.setKeyObtentionIterations(20);

        System.out.println("BigDecimal: " + enc3.getClass().getName());
        System.out.println("BigInteger: " + enc4.getClass().getName());

        System.out.println(this.enc3.getClass().getName());
        System.out.println(this.enc4.getClass().getName());

        String bteE = bte.encrypt("hello!");
        System.out.println("Basic encryptor: " + bte.getClass().getName() + bteE + " -> " + bte.decrypt(bteE));
        String steE = ste.encrypt("hello!");
        System.out.println("Strong encryptor: " + ste.getClass().getName() + steE + " -> " + ste.decrypt(steE));
        String encAESe = encAES.encrypt("hello!");
        System.out.println("AES: " + encAES.getClass().getName() + encAESe + " -> " + encAES.decrypt(encAESe));
        model.addAttribute("encAES", new String(encAESe) + " | " + encAES.getClass().getName());
        
        System.out.println(this.sd.digest("myPassword") + " | " + this.sd.getClass().getName());
        
        System.out.println(new String(this.bd.digest(mba)) + " | " + this.bd.getClass().getName());

        final String ep = this.bpe.encryptPassword("HELLO!!");
        System.out.println(ep + " | " + this.bpe.getClass().getName());
        
        final ConfigurablePasswordEncryptor configurablePasswordEncryptor = new ConfigurablePasswordEncryptor();
        configurablePasswordEncryptor.setAlgorithm("SHA-1");
        configurablePasswordEncryptor.setStringOutputType("0x");
        System.out.println(configurablePasswordEncryptor.checkPassword("HELLO!!", ep));
    
        System.out.println(this.eprop1);
        System.out.println(this.eprop2);
        System.out.println("----------");
        System.out.println(this.eprop1.getProperty("prop1"));
        System.out.println(this.eprop1.getProperty("prop2"));
        
        System.out.println("Existing digester: " + System.identityHashCode(this.sd));
        StringDigester stex = (StringDigester) this.appCtx.getBean("sd1");
        System.out.println("First digester: " + System.identityHashCode(stex));
        stex = (StringDigester) this.appCtx.getBean("sd1");
        System.out.println("Second digester: " + System.identityHashCode(stex));

        System.out.println("City name: " + this.cityName);
        System.out.println("Country name: " + this.countryName);

        return "index";
        
    }
    
}

