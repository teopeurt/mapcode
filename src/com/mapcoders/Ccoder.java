package com.mapcoders;

/**
 * Created with IntelliJ IDEA.
 * User: don
 * Date: 23/05/2014
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
/*
	class to convert between territory abbreviations and territory codes
	--------------------------------------------------------------------

		static String ccode2iso(int ccode,int international)
			returns iso code of territory 'ccode' (or "" in case or invalid ccode)
			if international=0, returns a 2- or 3-letter iso code (a 2 letter code MAY be ambiguous, e.g. "MN")
			if international=1, always returns a nonambiguous code by prefixing the parent country for any state
			if international=2, always returns a nonambiguous code by prefixing the parent country for a state WHEN NECESSARY

		static int iso2ccode(String isocode,int disambiguate_option)
			returns ccode for a given string (such as "MN" or "USA-MN" or "NLD")
			uses the value of disambiguate to choose between countries if isocode is an ambiguate state (e.g. MN could be USA-MN or IND-MN)
			Values are 0=don't care 1=USA 2=IND 3=CAN 4=AUS 5=MEX 6=BRA 7=RUS 8=CHI

		static boolean isState(int ccode)
			returns true iff ccode is a state in another country
		static boolean hasStates(int ccode) {
			returns true iff ccode is a country that has states
		static int StateParent(int ccode)
			returns parent country of ccode (or negative if ccode has no parent country, i.e. is not a state or province...)
		static int max_ccode()
			returns largest possible ccode (the smallest possible ccode is always 0)

*/
class Ccoder {

    public static int StateParent(int ccode) // returns parent ccode (or -1)
    {
        if (ccode>=usa_from && ccode<=usa_upto) return ccode_usa;
        if (ccode>=ind_from && ccode<=ind_upto) return ccode_ind;
        if (ccode>=can_from && ccode<=can_upto) return ccode_can;
        if (ccode>=aus_from && ccode<=aus_upto) return ccode_aus;
        if (ccode>=mex_from && ccode<=mex_upto) return ccode_mex;
        if (ccode>=bra_from && ccode<=bra_upto) return ccode_bra;
        if (ccode>=rus_from && ccode<=rus_upto) return ccode_rus;
        if (ccode>=chn_from && ccode<=chn_upto) return ccode_chn;
        return -1;
    }

    public static int max_ccode() { // returns largest possible ccode
        return (MAX_CCODE-1);
    }

    /// returns true iff ccode is a state
    public static boolean isState(int ccode) {
        return (StateParent(ccode)>=0);
    }

    /// returns true iff ccode is a country that has states
    public static boolean hasStates(int ccode) {
        return (ccode==ccode_usa || ccode==ccode_ind || ccode==ccode_can || ccode==ccode_aus || ccode==ccode_mex || ccode==ccode_bra || ccode==ccode_chn || ccode==ccode_rus);
    }

    public static String ccode2iso(int ccode,int international)
    {
        if (ccode<0 || ccode>=MAX_CCODE) return ""; else
        {
            String n=entity_iso3(ccode);
            final char firstc = n.charAt(0);
            if (firstc>='0' && firstc<='9') n=n.substring(1);
            final int len=n.length();
            if (international>0) {
                int p=stateletter(ccode);
                if (p>0)
                {
                    if (international==2) {
                        int count=0; // to count how often n occurs
                        int i=aliases.indexOf(n+'=');
                        if (i>=0)
                            count=2; // in alias! so assume more than once
                        else
                        {
                            int from=0;
                            for(;;) {
                                i=entity_iso.indexOf(n+',',from);
                                if (i<0) break;
                                if (len==3 || entity_iso.charAt(i-1)<='9') { count++; if (count>1) break; }
                                from=i+1;
                            }
                        }
                        if (count==1)
                            return n;
                    }
                    return parents2.substring(p*3-3,p*3-1) + '-' + n;
                }
            }
            return n;
        }
    }

    /* returns ccode, or negative if invalid */
    public static int iso2ccode(String isocode,int disambiguate_option)
    {
        isocode=isocode.trim().toUpperCase();

        String disambiguate=(disambiguate_option<2 || disambiguate_option>8 ? "1" : Integer.toString(disambiguate_option));

        int sep=isocode.lastIndexOf('-'); if (sep<0) sep=isocode.lastIndexOf(' ');
        if (sep>=0) {

            String prefix=isocode.substring(0,sep);
            isocode=isocode.substring(sep+1).trim();
            final int len=isocode.length();

            if (len!=2 && len!=3)
                return -1;

            // set disambiguation based on prefix
            int parent=parentletter(prefix);
            if (parent<0) return -2;
            disambiguate=Integer.toString(parent);

            if (len==2) {
                return iso2ccode(disambiguate+isocode,0);
            }
            else if (len==3)
            {
                String isoa=alias2iso(isocode);
                if (isoa.length()>0) {
                    if (isoa.charAt(0)==disambiguate.charAt(0))
                    {
                        isocode=isoa;
                    }
                }
            }

        }

        final int len=isocode.length();
        if (len!=2 && len!=3)
            return -1;

        {
            for(int j=0;j<=8;j++) {
                String c=(j==0 ? disambiguate : Integer.toString(j));
                String testiso=(len==2 ? c+isocode+',' : isocode+',');
                int i=entity_iso.indexOf(testiso);
                if (i>=0) return i/4;
                if (len==3) break;
            }

            isocode=alias2iso(isocode);
            if (isocode.length()>2)
                return iso2ccode(isocode,0);
        }

        return -1;
    }






    private static final String aliases = "2UK=2UT,2CG=2CT,1GU=GUM,1UM=UMI,1VI=VIR,1PR=PRI,1AS=ASM,1MP=MNP,4JB=JBT,4QL=QLD,4TS=TAS,4CX=CXR,4CC=CCK,4NF=NFK,4HM=HMD,4NI=NFK,COL=5CL,5ME=5MX,MEX=5MX,5TM=TAM,5AG=AGU,5BC=BCN,5BS=BCS,5CM=CAM,5CS=CHP,5CH=CHH,5CO=COA,5DF=DIF,5DG=DUR,5GT=GUA,5GR=GRO,5HG=HID,5JA=JAL,5MI=MIC,5MO=MOR,5NA=NAY,5NL=NLE,5OA=OAX,5PB=PUE,5QE=QUE,5QR=ROO,5SL=SLP,5SI=SIN,5SO=SON,5TB=TAB,5TL=TLA,5VE=VER,5YU=YUC,5ZA=ZAC,811=8BJ,812=8TJ,813=8HE,814=8SX,815=8NM,821=8LN,822=8JL,823=8HL,831=8SH,832=8JS,833=8ZJ,834=8AH,835=8FJ,836=8JX,837=8SD,841=8HA,842=8HB,843=8HN,844=8GD,845=8GX,846=8HI,850=8CQ,851=8SC,852=8GZ,853=8YN,854=8XZ,861=8SN,862=8GS,863=8QH,864=8NX,865=8XJ,871=TWN,891=HKG,892=MAC,8TW=TWN,8HK=HKG,8MC=MAC,BEL=7BE,KIR=7KI,PRI=7PO,CHE=7CH,KHM=7KM,PER=7PM,TAM=7TT,0US=USA,0AU=AUS,0RU=RUS,0CN=CHN,EAZ=TZA,SKM=2SK,TAA=SHN,ASC=SHN,DGA=IOT,WAK=MHL,JTN=UMI,MID=1HI,2OD=2OR,";

    private static final int MAX_CCODE = 541; /* total number of areas (i.e. recognised iso codes) in this database */
    private static final String entity_iso = ""
            +"VAT,MCO,GIB,TKL,CCK,BLM,NRU,TUV,MAC,SXM,MAF,NFK,PCN,BVT,BMU,IOT,SMR,GGY,AIA,MSR,JEY,CXR,WLF,VGB,LIE,ABW,MHL,ASM,COK,SPM,NIU,KNA,CYM,BES,MDV,SHN,MLT,GRD,VIR,MYT,SJM,VCT,HMD,BRB,ATG,CUW,SYC,PLW,MNP,AND,GUM,IMN,LCA,FSM,SGP,TON,DMA,BHR,KIR,TCA,STP,HKG,MTQ,FRO,"
            +"GLP,COM,MUS,REU,LUX,WSM,SGS,PYF,CPV,TTO,BRN,ATF,PRI,CYP,LBN,JAM,GMB,QAT,FLK,VUT,MNE,BHS,TLS,SWZ,KWT,FJI,NCL,SVN,ISR,PSE,SLV,BLZ,DJI,MKD,RWA,HTI,BDI,GNQ,ALB,SLB,ARM,LSO,BEL,MDA,GNB,TWN,BTN,CHE,NLD,DNK,EST,DOM,SVK,CRI,BIH,HRV,TGO,LVA,LTU,LKA,GEO,IRL,SLE,PAN,"
            +"CZE,GUF,ARE,AUT,AZE,SRB,JOR,PRT,HUN,KOR,ISL,GTM,CUB,BGR,LBR,HND,BEN,ERI,MWI,PRK,NIC,GRC,TJK,BGD,NPL,TUN,SUR,URY,KHM,SYR,SEN,KGZ,BLR,GUY,LAO,ROU,GHA,UGA,GBR,GIN,ECU,ESH,GAB,NZL,BFA,PHL,ITA,OMN,POL,CIV,NOR,MYS,VNM,FIN,COG,DEU,JPN,ZWE,PRY,IRQ,MAR,UZB,SWE,PNG,"
            +"CMR,TKM,ESP,THA,YEM,FRA,ALA,KEN,BWA,MDG,UKR,SSD,CAF,SOM,AFG,MMR,ZMB,CHL,TUR,PAK,MOZ,NAM,VEN,NGA,TZA,EGY,MRT,BOL,ETH,COL,ZAF,MLI,AGO,NER,TCD,PER,MNG,IRN,LBY,SDN,IDN,DIF,TLA,MOR,AGU,5CL,QUE,HID,5MX,TAB,NAY,GUA,PUE,YUC,ROO,SIN,CAM,MIC,SLP,GRO,NLE,BCN,VER,CHP,"
            +"BCS,ZAC,JAL,TAM,OAX,DUR,COA,SON,CHH,GRL,SAU,COD,DZA,KAZ,ARG,2DD,2DN,2CH,2AN,2LD,2DL,2ML,2NL,2MN,2TR,2MZ,2SK,2PB,2HR,2AR,2AS,2BR,2UT,2GA,2KL,2TN,2HP,2JK,2CT,2JH,2KA,2RJ,2OR,2GJ,2WB,2MP,2AP,2MH,2UP,2PY,NSW,ACT,JBT,4NT,4SA,TAS,VIC,4WA,QLD,6DF,6SE,6AL,6RJ,6ES,"
            +"6RN,6PB,6SC,6PE,6AP,6CE,6AC,6PR,6RR,6RO,6SP,6PI,6TO,6RS,6MA,6GO,6MS,6BA,6MG,6MT,6PA,6AM,1DC,1RI,1DE,1CT,1NJ,1NH,1VT,1MA,1HI,1MD,1WV,1SC,1ME,1IN,1KY,1TN,1VA,1OH,1PA,1MS,1LA,1AL,1AR,1NC,1NY,1IA,1IL,1GA,1WI,1FL,1MO,1OK,1ND,1WA,1SD,1NE,1KS,1ID,1UT,1MN,1MI,1WY,"
            +"1OR,1CO,1NV,1AZ,1NM,1MT,1CA,1TX,1AK,3BC,3AB,3ON,3QC,3SK,3MB,3NL,3NB,3NS,3PE,3YT,3NT,3NU,IND,AUS,BRA,USA,MEX,MOW,SPE,KGD,7IN,7AD,7SE,7KB,7KC,7CE,7CU,IVA,LIP,ORL,TUL,7BE,VLA,KRS,KLU,7TT,BRY,YAR,RYA,AST,MOS,SMO,7DA,VOR,NGR,PSK,KOS,STA,KDA,7KL,TVE,LEN,ROS,VGG,"
            +"VLG,MUR,7KR,NEN,7KO,ARK,7MO,NIZ,PNZ,7KI,7ME,ORE,ULY,7PM,7BA,7UD,7TA,SAM,SAR,YAN,7KM,SVE,TYU,KGN,7CH,7BU,ZAB,IRK,NVS,TOM,OMS,7KK,KEM,7AL,ALT,7TY,KYA,MAG,CHU,KAM,SAK,7PO,YEV,KHA,AMU,7SA,CAN,RUS,8SH,8TJ,8BJ,8HI,8NX,8CQ,8ZJ,8JS,8FJ,8AH,8LN,8SD,8SX,8JX,8HA,8GZ,"
            +"8GD,8HB,8JL,8HE,8SN,8NM,8HL,8HN,8GX,8SC,8YN,8XZ,8GS,8QH,8XJ,CHN,UMI,CPT,AT0,AT1,AT2,AT3,AT4,AT5,AT6,AT7,AT8,ATA,AAA,";

    private static final int usa_from   =342;
    private static final int usa_upto   =392;
    private static final int ccode_usa  =409;
    private static final int ind_from   =271;
    private static final int ind_upto   =305;
    private static final int ccode_ind  =406;
    private static final int can_from   =393;
    private static final int can_upto   =405;
    private static final int ccode_can  =494;
    private static final int aus_from   =306;
    private static final int aus_upto   =314;
    private static final int ccode_aus  =407;
    private static final int mex_from   =233;
    private static final int mex_upto   =264;
    private static final int ccode_mex  =410;
    private static final int bra_from   =315;
    private static final int bra_upto   =341;
    private static final int ccode_bra  =408;
    private static final int chn_from   =496;
    private static final int chn_upto   =526;
    private static final int ccode_chn  =527;
    private static final int rus_from   =411;
    private static final int rus_upto   =493;
    private static final int ccode_rus  =495;
    private static final int ccode_ata  =539;
    private static final int ccode_earth=540;

    private static final String parents3="USA,IND,CAN,AUS,MEX,BRA,RUS,CHN,";
    private static final String parents2="US,IN,CA,AU,MX,BR,RU,CN,";








    private static int stateletter(int ccode) // return parent index (or 0)
    {
        if (ccode>=usa_from && ccode<=usa_upto) return 1; //ccode_usa
        if (ccode>=ind_from && ccode<=ind_upto) return 2; //ccode_ind
        if (ccode>=can_from && ccode<=can_upto) return 3; //ccode_can
        if (ccode>=aus_from && ccode<=aus_upto) return 4; //ccode_aus
        if (ccode>=mex_from && ccode<=mex_upto) return 5; //ccode_mex
        if (ccode>=bra_from && ccode<=bra_upto) return 6; //ccode_bra
        if (ccode>=rus_from && ccode<=rus_upto) return 7; //ccode_rus
        if (ccode>=chn_from && ccode<=chn_upto) return 8; //ccode_chn
        return 0;
    }

    /* returns clean 3-letter code with no processing whatsoever */
    private static String entity_iso3(int ccode) {
        if (ccode<0 || ccode>=MAX_CCODE) ccode=ccode_earth;
        return entity_iso.substring(ccode*4,ccode*4+3);
    }

    private static int parentletter(String isocode) {
        isocode=isocode.trim().toUpperCase();
        int len=isocode.length();
        if (len!=2 && len!=3) return -2;
        int p= (len==2 ? parents2.indexOf(isocode+',') : parents3.indexOf(isocode+','));
        if (p<0)
            return -2;
        return 1 + (p/(len+1));
    }

    private static boolean mIsUsaState( int ccode )       { return (ccode>=usa_from && ccode<=usa_upto); }
    private static boolean mIsIndiaState( int ccode )     { return (ccode>=ind_from && ccode<=ind_upto); }
    private static boolean mIsCanadaState( int ccode )    { return (ccode>=can_from && ccode<=can_upto); }
    private static boolean mIsAustraliaState( int ccode ) { return (ccode>=aus_from && ccode<=aus_upto); }
    private static boolean mIsMexicoState( int ccode )    { return (ccode>=mex_from && ccode<=mex_upto); }
    private static boolean mIsBrazilState( int ccode )    { return (ccode>=bra_from && ccode<=bra_upto); }
    private static boolean mIsChinaState( int ccode )     { return (ccode>=chn_from && ccode<=chn_upto); }
    private static boolean mIsRussiaState( int ccode )    { return (ccode>=rus_from && ccode<=rus_upto); }



    private static String alias2iso(String isocode) {
        int p,from=0;
        int len=isocode.length();
        isocode+='=';
        for(;;)
        {
            p = aliases.indexOf(isocode,from);
            if (p<0 || len<2 || len>3) return "";
            if (len==3) break; /* p is correct! */
            p--; char c=aliases.charAt(p); if (c>='0' && c<='9') break;
            from=p+7;
        }
        return aliases.substring(p+4,p+7);
    }

} // Ccoder

