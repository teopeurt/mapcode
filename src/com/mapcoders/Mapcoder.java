package com.mapcoders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: don
 * Date: 23/05/2014
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */

/*
	mapcode encoding/decoding
	-------------------------
	public ArrayList<String> master_encode(double lat,double lon,int ccode,boolean add_to_previous_calls,boolean stop_with_one_result,boolean allowworld)
		pass latitude and longitude (lat,lon) in degrees
		pass a valid ccode (e.g. as acquired through Ccoder.iso2ccode)
		pass stop_with_one_result=true to just a single mapcode (i.e. the first one discovered, which is also the shortest in the ccode territory)
		pass allowworld=true to also get the 10-letter "international" mapcode as an alternative
		pass add_to_previous_calls=false, unless you wish the results to be added to the results of previous master_encode calls


*/




class Mapcoder {

    private static final int ccode_usa  =409;
    private static final int ccode_ind  =406;
    private static final int ccode_can  =494;
    private static final int ccode_aus  =407;
    private static final int ccode_mex  =410;
    private static final int ccode_bra  =408;
    private static final int ccode_chn  =527;
    private static final int ccode_rus  =495;
    private static final int ccode_earth=540;

    private static int datasize; // size
    static byte[] fileData;

    public Mapcoder() {

        // *** read whole file into memory ***
        try {
            Path path = Paths.get("mminfo.dat");
            fileData = Files.readAllBytes(path);
            datasize = fileData.length;
        } catch(IOException e) {
            datasize = 0;
            System.out.printf("Failed to load data file!\r\n");
        }

    }

    public final static String version() { return "1.00"; }

// ========================= encode/decode ===========================

    private static int [] xdivider19 = {
            360,  360,  360,  360,  360,  360,  361,  361,  361,  361,
            362,  362,  362,  363,  363,  363,  364,  364,  365,  366,
            366,  367,  367,  368,  369,  370,  370,  371,  372,  373,
            374,  375,  376,  377,  378,  379,  380,  382,  383,  384,
            386,  387,  388,  390,  391,  393,  394,  396,  398,  399,
            401,  403,  405,  407,  409,  411,  413,  415,  417,  420,
            422,  424,  427,  429,  432,  435,  437,  440,  443,  446,
            449,  452,  455,  459,  462,  465,  469,  473,  476,  480,
            484,  488,  492,  496,  501,  505,  510,  515,  520,  525,
            530,  535,  540,  546,  552,  558,  564,  570,  577,  583,
            590,  598,  605,  612,  620,  628,  637,  645,  654,  664,
            673,  683,  693,  704,  715,  726,  738,  751,  763,  777,
            791,  805,  820,  836,  852,  869,  887,  906,  925,  946,
            968,  990, 1014, 1039, 1066, 1094, 1123, 1154, 1187, 1223,
            1260, 1300, 1343, 1389, 1438, 1490, 1547, 1609, 1676, 1749,
            1828, 1916, 2012, 2118, 2237, 2370, 2521, 2691, 2887, 3114,
            3380, 3696, 4077, 4547, 5139, 5910, 6952, 8443,10747,14784,
            23681,59485
    };

    private static int x_divider(int miny,int maxy) // returns divider for longitude (multiplied by 4), for a given latitude
    {
        if (miny>=0) // maxy>miny>0
            return xdivider19[miny>>19];
        if (maxy>=0) // maxy>0>miny
            return xdivider19[0];
        return xdivider19[(-maxy)>>19]; // 0>maxy>miny
    }

    private final static int[] nc = { 1, 31, 961, 29791, 923521, 28629151, 887503681 };

    private final static int[] xside = { 0, 5,  31,  168,    961,    168*31,     29791,       165869,         923521,        5141947};

    private final static int[] yside = { 0, 6,  31,  176,    961,    176*31,     29791,       165869,         923521,        5141947};

    private final static byte[] decode_chars = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            0,  1,  2,  3,  4,  5,  6,  7,  8,  9, -1, -1, -1, -1, -1, -1,
            -1, -2, 10, 11, 12, -3, 13, 14, 15,  1, 16, 17, 18, 19, 20,  0,
            21, 22, 23, 24, 25, -4, 26, 27, 28, 29, 30, -1, -1, -1, -1, -1,
            -1, -2, 10, 11, 12, -3, 13, 14, 15,  1, 16, 17, 18, 19, 20,  0,
            21, 22, 23, 24, 25, -4, 26, 27, 28, 29, 30, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

    private final static char[] encode_chars = {
            '0','1','2','3','4','5','6','7','8','9',
            'B','C','D','F','G','H','J','K','L','M',
            'N','P','Q','R','S','T','V','W','X','Y','Z'
    };

    private final static int[] data_start = {

            0,    3,    6,    9,   13,   16,   18,   19,   30,   31,
            33,   35,   37,   42,   44,   47,   51,   54,   56,   58,
            60,   62,   64,   68,   71,   79,   81,  116,  121,  131,
            133,  135,  138,  143,  155,  159,  165,  167,  169,  173,
            176,  183,  185,  187,  189,  193,  195,  202,  208,  212,
            216,  219,  221,  223,  250,  252,  259,  261,  266,  284,
            288,  292,  296,  298,  303,  311,  315,  325,  327,  332,
            336,  342,  356,  360,  365,  369,  385,  391,  396,  404,
            412,  428,  432,  436,  443,  454,  491,  495,  521,  527,
            545,  557,  593,  659,  694,  733,  745,  754,  782,  798,
            832,  848,  882,  918,  932,  956,  967, 1022, 1080, 1094,
            1155, 1179, 1208, 1276, 1353, 1405, 1484, 1569, 1621, 1696,
            1772, 1833, 1875, 1900, 1948, 1991, 2036, 2097, 2183, 2270,
            2319, 2382, 2467, 2558, 2643, 2702, 2815, 2912, 3004, 3088,
            3186, 3248, 3350, 3416, 3511, 3602, 3688, 3776, 3866, 3973,
            4119, 4221, 4325, 4356, 4393, 4468, 4504, 4538, 4574, 4626,
            4690, 4741, 4756, 4791, 4931, 5017, 5067, 5143, 5168, 5236,
            5293, 5328, 5408, 5459, 5530, 5623, 5658, 5736, 5795, 5861,
            5918, 5971, 6028, 6065, 6156, 6285, 6335, 6365, 6417, 6513,
            6546, 6627, 6675, 6755, 6798, 6892, 6926, 6971, 7078, 7083,
            7155, 7199, 7283, 7323, 7347, 7404, 7490, 7564, 7603, 7690,
            7766, 7874, 7952, 8026, 8096, 8153, 8235, 8317, 8376, 8403,
            8441, 8495, 8571, 8681, 8761, 8825, 8882, 8936, 9014, 9085,
            9171, 9221, 9282, 9426, 9430, 9435, 9440, 9445, 9451, 9456,
            9464, 9472, 9483, 9491, 9506, 9515, 9526, 9559, 9568, 9576,
            9591, 9601, 9611, 9619, 9627, 9643, 9651, 9661, 9672, 9682,
            9692, 9701, 9710, 9721, 9762, 9803, 9821, 9918,10069,10160,
            10242,10437,10440,10443,10446,10456,10461,10464,10476,10489,
            10500,10516,10525,10531,10565,10593,10612,10680,10772,10787,
            10791,10827,10914,10925,10943,10960,11024,11056,11097,11117,
            11222,11302,11366,11425,11481,11541,11547,11589,11593,11596,
            11606,11621,11632,11670,11733,11779,11784,11791,11796,11815,
            11825,11832,11840,11854,11870,11877,11885,11893,11941,11950,
            11961,12011,12022,12032,12051,12067,12080,12092,12111,12145,
            12154,12166,12178,12182,12188,12194,12206,12217,12226,12232,
            12246,12264,12275,12317,12368,12418,12474,12487,12500,12563,
            12623,12684,12749,12820,12885,12954,13029,13096,13175,13249,
            13263,13278,13323,13336,13364,13378,13391,13407,13423,13435,
            13477,13489,13500,13535,13550,13562,13579,13587,13625,13638,
            13653,13773,13819,13863,13903,13927,13968,14007,14027,14043,
            14061,14073,14087,14095,14106,14123,14136,14191,14192,14193,
            14194,14233,14239,14244,14249,14254,14260,14267,14273,14282,
            14288,14296,14302,14309,14318,14326,14335,14347,14356,14364,
            14375,14386,14395,14405,14412,14462,14496,14505,14517,14528,
            14567,14578,14592,14609,14621,14634,14648,14663,14676,14697,
            14705,14724,14733,14745,14756,14770,14785,14798,14812,14825,
            14839,14854,14872,14892,14904,14924,14937,14952,14966,14984,
            15000,15014,15027,15044,15064,15081,15099,15114,15128,15144,
            15155,15171,15184,15199,15214,15260,15274,15283,15293,15306,
            15320,15331,15344,15360,15375,15376,15403,15409,15417,15423,
            15439,15476,15493,15517,15540,15557,15577,15603,15629,15648,
            15665,15718,15759,15781,15808,15828,15849,15867,15897,15923,
            15943,15964,15982,16000,16019,16035,16051,16079,16105,16112,
            16114,16115,16117,16118,16120,16122,16124,16126,16128,16129,
            16130,16162};

    /// low-level routines for data access
    private static int dataFirstRecord(int ccode) { return data_start[ccode]; }
    private static int dataLastRecord(int ccode)  { return data_start[++ccode]-1; }

    /// returns true iff x in range [minx...maxx> in millionths
    private static boolean isInRange(int x,int minx,int maxx)
    {
        if (minx<=x && x<maxx) return true;
        if (x<180000000) x+=360000000; else x-=360000000;
        if (minx<=x && x<maxx) return true;
        return false;
    }


    private int minx,miny,maxx,maxy;
    private int flags,codex,codexlo,codexhi,codexlen;
    private boolean iscountry,isnameless,isuseless,isSpecialShape22;
    private int pipetype;
    private String pipeletter;

    /* @@@ DIRECT BYTE DATA ACCESS */
    private static int _ubyte(int i) { int u=fileData[i]; if(u<0) u+=256; return u; }
    private static int _long(int i) { return _ubyte(i) + (_ubyte(i+1)<<8) + (_ubyte(i+2)<<16) + (_ubyte(i+3)<<24); }
    private static int smartdiv(int i)   { return (_ubyte(i*20+18) + _ubyte(i*20+19)*256); }
    private static int data_flags(int i) { return (_ubyte(i*20+16) + _ubyte(i*20+17)*256); }
    private void minmaxSetup(int arg) {
        int i=arg*20; minx=_long(i);
        i+=4; miny=_long(i);
        i+=4; maxx=_long(i);
        i+=4; maxy=_long(i);
    }


    private static boolean isUseless(int i) {
        return (data_flags(i) & 512)!=0;
    }
    private static boolean isNameless(int i) {
        return (data_flags(i) & 64)!=0;
    }
    private static boolean isStarpipe(int i) {
        return (data_flags(i) & (8<<5))!=0;
    }
    private int CodexLen(int i) {
        flags = data_flags(i);
        codexhi = ((flags & 31)/5);
        return codexhi + ((flags & 31)%5) + 1;
    }
    private int Codex(int i) {
        flags = data_flags(i);
        codexhi = ((flags & 31)/5);
        return 10*codexhi + ((flags & 31)%5) + 1;
    }
    private static boolean isSpecialShape(int i) {
        return (data_flags(i) & 1024)!=0;
    }

    private void dataSetup(int i) {
        flags = data_flags(i);
        codexhi = ((flags & 31)/5);
        codexlo = ((flags & 31)%5) + 1;
        codexlen = codexlo+codexhi;
        codex = 10*codexhi + codexlo;
        iscountry  = (flags & 32)!=0;
        isnameless = (flags & 64)!=0;
        isuseless  = (flags & 512)!=0;
        isSpecialShape22 = (flags & 1024)!=0;
        pipetype   = ((flags>>5) & 12); // 4=pipe 8=plus 12=star
        if ( pipetype==4 )
            pipeletter=Character.toString(encode_chars[(flags>>11)&31]);
        if (codex==21) if (!isnameless) { codex++; codexlo++; codexlen++; }
        minmaxSetup(i);
    }

    private int first_nameless_record;
    private int count_city_coordinates_for_country(int samecodex,int index,int firstcode)
    {
        int i=index;
        while ( i>=firstcode && isNameless(i) && Codex(i)==samecodex ) i--;
        i++; first_nameless_record=i;
        int e=index;
        while ( Codex(e)==samecodex ) e++;
        e--;
        return (e-i+1);
    }


// --------------------- coding/decoding ------------------

    /// lowest level encode/decode routines
    private static int fast_decode(String code) // decode up to dot or EOS; returns negative in case of error
    {
        int value = 0;
        int i;
        for ( i=0; i<code.length(); i++ )
        {
            int c = (int)code.charAt(i);
            if ( c==46 ) // dot!
                return value;
            if ( decode_chars[c]<0 )
                return -1;
            value = value*31 + decode_chars[c];
        }
        return value;
    }

    private static String fast_encode(int value,int nrchars)
    {
        String result="";
        while ( nrchars-- > 0 )
        {
            result = encode_chars[ value % 31 ] + result;
            value = (value / 31);
        }
        return result;
    }

    private Point decode_triple(String str)
    {
        final byte c1 = decode_chars[(int)str.charAt(0)];
        final int x = fast_decode(str.substring(1));
        if ( c1<24 )
            return new Point( (c1%6) * 28 + (x/34) , (c1/6) * 34 + (x%34) );
        return new Point( (x/40) + 24*(c1-24) , (x%40) + 136 );
    }

    private static String encode_triple(int difx,int dify)
    {
        if ( dify < 4*34 )
            return encode_chars[((difx/28) + 6*(dify/34)) ] + fast_encode(  (difx%28)*34+(dify%34 ), 2 );
        else
            return encode_chars[ (difx/24) + 24           ] + fast_encode(  (difx%24)*40+(dify-136), 2 );
    }

    private Point decode6(int v,int width,int height)
    {
        int D=6;
        int col = (v/(height*6));
        int maxcol = ((width-4)/6);
        if ( col>=maxcol )
        {
            col=maxcol;
            D = width-maxcol*6;
        }
        int w = v - (col * height * 6 );
        return new Point( col*6 + (w%D) , height-1-(w/D) );
    }

    private static int encode6(int x,int y,int width,int height)
    {
        int D=6;
        int col = (x/6);
        int maxcol = ((width-4)/6);
        if ( col>=maxcol )
        {
            col=maxcol;
            D = width-maxcol*6;
        }
        return (height*6*col) + (height-1-y)*D + (x-col*6);
    }

    private int use_high_precision=0; // nr of letters of high-precision postfix (if any)
    private String extrapostfix="";
    private String addpostfix(String result,int extrax4,int extray,int dividerx4,int dividery)
    {
        if (use_high_precision==0)
            return result;

        int gx=((30*extrax4)/dividerx4);
        int gy=((30*extray )/dividery );
        int x1=(gx/6); int x2=(gx%6);
        int y1=(gy/5); int y2=(gy%5);

        extrapostfix=Character.toString(encode_chars[y1*5+x1]);
        if (use_high_precision==2) extrapostfix+=encode_chars[ y2*6+x2 ];
        return result+'-'+extrapostfix;
    }

    private Point add2res(int y,int x,int dividerx4,int dividery,int ydirection)
    {
        if (extrapostfix.length()>0) {
            int c1 = (int)extrapostfix.charAt(0);
            c1=decode_chars[c1];
            if (c1<0) c1=0; else if (c1>29) c1=29;
            int y1 = (c1/5); int x1 = (c1%5);
            int c2 = (extrapostfix.length()==2) ? (int)extrapostfix.charAt(1) : 72; // 72='H'=code 15=(3+2*6)
            c2 = decode_chars[c2];
            if (c2<0) c2=0; else if (c2>29) c2=29;
            int y2 = (c2/6); int x2 = (c2%6);

            int extrax = ( ((x1*12 + 2*x2 + 1)*dividerx4+120)/240);
            int extray = ( ((y1*10 + 2*y2 + 1)*dividery  +30)/ 60);

            return new Point( x+extrax, y+extray*ydirection );
        }
        return new Point( x+(dividerx4/8), y+(dividery/2)*ydirection );
    }

    private static final int zonedata=-1;
    private Point addzonedata(int rely,int relx,int ygridsize,int xgridsize,String RESULT,int ccode,int c1,int dividerx,int dividery)
    {
// @@@		zonedata = [rely,relx,rely+ygridsize,relx+xgridsize,RESULT+(RESULT.indexOf('.')<0 ? '.' : "")+(c1<0 ? "" : encode_chars[c1]),ccode];
        if (c1>=0) {
            int dx,dy;
            int nx,ny;
            if ( c1<24 ) {
                nx =           (c1%6) * 28; dx=28;
                ny = (c1/6) * 34; dy=34;
            }
            else {
                nx = 24*(c1-24); dx=24;
                ny = 136;  dy=40;
            }

            int zonedata8 = rely+ygridsize-(ny*dividery);
            int zonedata6 = zonedata8-(dy*dividery);
            int zonedata7 = relx+(nx*dividerx);
            int zonedata9 = zonedata7+(dx*dividerx);

/* @@@
			zonedata[6] = zonedata6;
			zonedata[7] = zonedata7;
			zonedata[8] = zonedata8;
			zonedata[9] = zonedata9;
@@@ */

            return new Point( (zonedata7+zonedata9)/2, (zonedata6+zonedata8)/2 );
        }
        else
        {
            return new Point( (relx+xgridsize/2), (rely+ygridsize/2) );
        }
    }


    // mid-level encode/decode
    private String encode_nameless(int x,int y,int index,int firstcode) // returns "" in case of (argument) error
    {
        int A = count_city_coordinates_for_country(codex,index,firstcode);
        int p = (31/A);
        int r = (31 % A);
        int X = index - first_nameless_record;

        if (A>1)
        {
            int storage_offset=0;

            if ( codex!=21 && A<=31 )
            {
                int size=p; if (X<r) size++;
                storage_offset = (X*p + (X<r ? X : r)) * (961*961);
            }
            else if ( codex!=21 && A<62 )
            {
                if ( X < (62-A) )
                {
                    storage_offset = X*(961*961);
                }
                else
                {
                    storage_offset = (62-A + ((X-62+A)/2) )*(961*961);
                    if ( ((X+A) & 1) !=0 )
                    {
                        storage_offset += (16*961*31);
                    }
                }
            }
            else
            {
                int BASEPOWER = (codex==21) ? 961*961 : 961*961*31;
                int BASEPOWERA = (BASEPOWER/A);
                if (A==62)
                    BASEPOWERA++;
                else
                    BASEPOWERA = (961) * (BASEPOWERA/961);

                storage_offset = X * BASEPOWERA;
            }

            int SIDE=smartdiv(index);
            int orgSIDE=SIDE;
            int xSIDE=SIDE;
            if ( isSpecialShape22 )
            {
                xSIDE *= SIDE;
                SIDE = 1+((maxy-miny)/90);
                xSIDE = (xSIDE/SIDE);
            }

            int dividerx4 = x_divider(miny,maxy); // 4 times too large
            int dx = ( (4*(x-minx))/dividerx4 ); // div with floating point value
            int extrax4 = (x-minx)*4 - dx*dividerx4; // mod with floating point value

            int dividery = 90;
            int dy = ( (maxy-y)/dividery );
            int extray = (maxy-y) % dividery;

            int v = storage_offset;
            if ( isSpecialShape22 )
                v += encode6(dx,SIDE-1-dy,xSIDE,SIDE);
            else
                v+= (dx*SIDE + dy);

            String result = fast_encode( v, codexlen+1 );

            if ( codexlen==3 )
            {
                result = result.substring(0,2) + '.' + result.substring(2);
            }
            else if ( codexlen==4 )
            {
                if ( codex==22 && A<62 && orgSIDE==961 && !isSpecialShape22 )
                    result = result.substring(0,2) + result.charAt(3) + result.charAt(2) + result.charAt(4);
                if (codex==13)
                    result = result.substring(0,2) + '.' + result.substring(2);
                else
                    result = result.substring(0,3) + '.' + result.substring(3);
            }

            return addpostfix(result,extrax4,extray,dividerx4,dividery);
        }
        return "";
    }

    private String encode_grid(int m,int y,int x,int codex,int minx,int miny,int maxx,int maxy,String pipe,int ccode)
    {
        int orgcodex=codex; if (codex==14) codex=23;
        int dc = (codex/10);
        int codexlow = (codex%10);
        int codexlen = dc + codexlow;

        int divx,divy = smartdiv(m);
        if (divy==1) {divx = xside[dc];divy = yside[dc];} else divx = ( nc[dc] / divy );



        int ygridsize = ((maxy-miny+divy-1)/divy);
        int rely = y-miny;
        rely = (rely/ygridsize);
        int xgridsize = ((maxx-minx+divx-1)/divx);

        int relx = x-minx;
        if (relx<0) { x+=360000000; relx+=360000000; }
        if (relx<0)
            return "";
        relx = ( relx/xgridsize);
        if (relx>=divx)
            return "";

        int v;
        if ( divx!=divy && codex>24 ) // D==6
        {
            v = encode6(relx,rely,divx,divy);
        }
        else
        {
            v = relx*divy + (divy-1-rely);
        }

        String result = fast_encode( v, dc );


        if ( dc==4 && divx==xside[4] && divy==yside[4] )
            result = "" + result.charAt(0) + result.charAt(2) + result.charAt(1) + result.charAt(3);

        rely = miny + (rely*ygridsize);
        relx = minx + (relx*xgridsize);

        int dividery = ( (((ygridsize))+yside[codexlow]-1)/yside[codexlow] );
        int dividerx = ( (((xgridsize))+xside[codexlow]-1)/xside[codexlow] );

        result += '.';

        // encoderelative

        int nrchars=codexlow;

        int  difx = x-relx;
        int  dify = y-rely;
        int  extrax = difx % dividerx;
        int  extray = dify % dividery;
        difx = (difx/dividerx);
        dify = (dify/dividery);

        dify = yside[nrchars]-1-dify;
        if ( nrchars==3 )
        {
            result += encode_triple(difx,dify);
        }
        else
        {

            String postfix = fast_encode( (difx)*yside[nrchars]+dify, nrchars );
            if ( nrchars==4 )
            {
                postfix = "" + postfix.charAt(0) + postfix.charAt(2) + postfix.charAt(1) + postfix.charAt(3);
            }
            result += postfix;
        }
        // encoderelative

        if (orgcodex==14) {
            result = result.charAt(0)+"."+result.charAt(1)+result.substring(3);
        }

/* @@@
		if (codexlow==3 || codex==44)
		if (zonedata==0 || zonedata==1) {
			int c1 = (zonedata==0 || codexlow!=3) ? -1 : decode_chars[(int)result.charAt(dc+1)];
			addzonedata(rely,relx,dividery*yside[codexlow],dividerx*xside[codexlow],pipe+result.substring(0,dc+1),ccode,c1,dividerx,dividery);
		}
@@@ */

        return addpostfix(pipe+result,extrax<<2,extray,dividerx<<2,dividery);
    }


    private int first_encode_record=-1;
    private ArrayList<String> results = new ArrayList<String>();


    private String encode_starpipe(int y,int x,int thiscodexlen,int thisindex,int ccode)
    {
        String starpipe_result;
        boolean done=false;
        int STORAGE_START=0;

        // search back to first pipe star
        int firstindex = thisindex;
        while ( isStarpipe(firstindex-1) && CodexLen(firstindex-1)==thiscodexlen )
            firstindex--;

        starpipe_result="";

        for(int i=firstindex;;i++)
        {
            if (CodexLen(i)!=thiscodexlen) {
                return starpipe_result;
            }

            dataSetup(i);
            if (!done)
            {
                int H = ((maxy-miny+89)/90);
                int xdiv = x_divider(miny,maxy);
                int W = ( ( (maxx-minx)*4 + (xdiv-1) ) / xdiv );

                H = 176*( (H+176-1)/176 );
                W = 168*( (W+168-1)/168 );

                int product = (W/168)*(H/176)*961*31;

                int GOODROUNDER = codex>=23 ? (961*961*31) : (961*961);
                if ( pipetype==8 ) // *+
                    product = ((STORAGE_START+product+GOODROUNDER-1)/GOODROUNDER) * GOODROUNDER - STORAGE_START;

                if ( i==thisindex )
                    if ( miny<=y && y<maxy && isInRange(x,minx,maxx) )
                    {
                        int dividerx = ((maxx-minx+W-1)/W);
                        int vx = ((x-minx)/dividerx);
                        int extrax =       ((x-minx)%dividerx);

                        int dividery = ((maxy-miny+H-1)/H);
                        int vy = ((maxy-y)/dividery);
                        int extray =       ((maxy-y)%dividery);


                        int spx = vx%168;
                        int spy = vy%176;

                        vx = (vx/168);
                        vy = (vy/176);

                        // PIPELETTER ENCODE
                        int value = (vx * (H/176) + vy);

                        starpipe_result = fast_encode( (STORAGE_START/(961*31)) + value, codexlen-2 );
                        starpipe_result += ".";
                        starpipe_result += encode_triple(spx,spy);

/* @@@
					if (zonedata==0 || zonedata==1) {
						int c1 = (zonedata==0) ? -1 : decode_chars[(int)starpipe_result.charAt(starpipe_result.length-3)];
						addzonedata(maxy-(vy+1)*dividery*176,minx+vx*dividerx*168,176*dividery,168*dividerx,starpipe_result.substring(0,starpipe_result.length-3),ccode,c1,dividerx,dividery);
					}
@@@ */

                        if (results.size()==0) first_encode_record=i;

                        starpipe_result = addpostfix(starpipe_result,extrax<<2,extray,dividerx<<2,dividery);
                        done=true; // will be returned soon, but look for end of pipes
                    }
                STORAGE_START += product;

            } //!done
        } // for i
    }


    private static String aeu_pack(String r)
    {
        int dotpos=-9;
        int rlen=r.length();
        int d;
        String rest="";
        for ( d=0;d<rlen;d++ )
            if ( r.charAt(d)<'0' || r.charAt(d)>'9' ) // not digit?
                if ( r.charAt(d)=='.' && dotpos<0 ) // first dot?
                    dotpos=d;
                else if ( r.charAt(d)=='-' ) {
                    rest=r.substring(d);
                    r=r.substring(0,d);
                    rlen=d;
                }
                else
                    return r; // not alldigit (or multiple dots)

        if (rlen-2 > dotpos) { // does r have a dot, AND at least 2 chars after the dot?
            int v = (((int)r.charAt(rlen-2))-48)*10+(((int)r.charAt(rlen-1))-48);
            int last  = (v%34);
            char [] vowels = {'A','E','U'};
            r = r.substring(0,rlen-2) + vowels[(v/34)] + (last<31 ? encode_chars[last] : vowels[last-31]);
        }
        return r+rest;
    }



    private int state_override=-1;
    public ArrayList<String> master_encode(double orgy,double orgx,int ccode,boolean isrecursive,boolean stop_with_one_result,boolean allowworld)
    {
        if (!isrecursive) {results.clear();first_encode_record=-1;}
        int first_result_index=results.size();

        if (ccode<0 || ccode>ccode_earth) ccode=ccode_earth;
        if (orgy>90) orgy-=180; else if (orgy<-90) orgy+=180;
        if (orgx>179.999999) orgx-=360; else if (orgx<-180) orgx+=180;

        int from = dataFirstRecord(ccode);
        if (data_flags(from)==0) return results;
        int upto = dataLastRecord(ccode);

        int y = (int)Math.round(orgy*1000000);
        int x = (int)Math.round(orgx*1000000);

        // LIMIT_TO_OUTRECT : make sure it fits the country
        if ( ccode!=ccode_earth )
        {
            minmaxSetup(upto); // find encompassing rect
            if ( ! ( miny<=y && y<maxy && isInRange(x,minx,maxx) ) ) // no fit?
            {
                if (isrecursive)
                    return results;
                from=upto+1; // empty the range
            }
        }



        int i;
        for ( i=from; i<=upto; i++ )
        {
            dataSetup(i);
            if ( codex<54 ) // exlude 54 and 55
            {
                if ( miny<=y && y<maxy && isInRange(x,minx,maxx) )
                {
                    String r="";
                    if (results.size()==0) first_encode_record=i;
                    if ( isuseless && i==upto && Ccoder.StateParent(ccode)>=0 ) {
						/* if ( ! isrecursive ) */ { // 1.11 allow country-wide mapcodes in states
                            state_override=ccode;
                            master_encode(orgy,orgx,Ccoder.StateParent(ccode),true,stop_with_one_result,allowworld);
                            state_override=-1;
                        }
                        continue;
                    }
                    else if (pipetype==0 && !isnameless) {
                        if (isuseless && results.size()==first_result_index) {
                            // RESTRICTUSELESS : ignore! nothing was found yet in non-useless records!
                        }
                        else
                        {
                            r = encode_grid(i,y,x,codex,minx,miny,maxx,maxy,"",ccode);
                        }
                    }
                    else if (pipetype==4) {
                        r = encode_grid(i,y,x,codex,minx,miny,maxx,maxy,pipeletter,ccode);
                    }
                    else if (isnameless) { // auto-pipe 21/22
                        r = encode_nameless(x,y,i,from);
                    }
                    else { // pipe star, pipe plus
                        r = encode_starpipe(y,x,codexlen,i,ccode);
                    }


                    if ( r.length()>4 )
                    {
                        r=aeu_pack(r);

                        int storecode=ccode;
                        if(state_override>=0)
                            storecode=state_override;

                        results.add( r+"/"+storecode );

                        if (stop_with_one_result)
                            return results;


                    }
                } // in rect
            }
        }

        if (allowworld)
            if (!isrecursive)
                if ( ccode!=ccode_earth )
                    master_encode(orgy,orgx,ccode_earth,true,stop_with_one_result,false);

        return results;
    }



    private Point decode_grid( String result, int minx,int miny,int maxx,int maxy, /*char pipe,*/ int ccode,int m ) // for a well-formed result, and integer variables
    {
        int relx,rely;
        String orgresult=result;
        int codexlen = result.length() - 1; // length ex dot
        int dc = result.indexOf('.'); // dotposition


        if (dc==1 && codexlen==5) {
            dc++; result=result.substring(0,1)+result.charAt(2)+'.'+result.substring(3);
        }
        int codexlow = codexlen-dc;
        int codex = 10*dc + codexlow;

        int divx,divy;
        divy = smartdiv(m);
        if (divy==1) {divx = xside[dc];divy = yside[dc];} else divx = ( nc[dc] / divy );

        if ( dc==4 && divx==xside[4] && divy==yside[4] )
        {
            result = result.substring(0,1) + result.charAt(2) + result.charAt(1) + result.substring(3);
        }

        int v = fast_decode(result);

        if ( divx!=divy && codex>24 ) // D==6
        {
            Point d = decode6(v,divx,divy);
            relx=d.x;
            rely=d.y;
        }
        else
        {
            relx=(v/divy);
            rely=v % divy;
            rely=divy-1-rely;
        }

        int ygridsize = ((maxy-miny+divy-1)/divy);
        int xgridsize = ((maxx-minx+divx-1)/divx);

        rely = miny + (rely*ygridsize);
        relx = minx + (relx*xgridsize);

        int dividery = (( (((ygridsize))+yside[codexlow]-1)/yside[codexlow] ));
        int dividerx = (( (((xgridsize))+xside[codexlow]-1)/xside[codexlow] ));

        String rest = result.substring(dc+1);

/* @@@
		if (zonedata==0 || zonedata==1) {
			int c1 = (zonedata==0 || rest.length!=3) ? -1 : decode_chars[(int)rest.charAt(0)];
			return addzonedata(rely,relx,dividery*yside[codexlow],dividerx*xside[codexlow],pipe+orgresult.substring(0,dc),ccode,c1,dividerx,dividery);
		}
@@@ */

        // decoderelative (postfix vs rely,relx)
        int difx;
        int dify;
        int nrchars = rest.length();

        if ( nrchars==3 )
        {
            Point d = decode_triple(rest);
            difx=d.x;
            dify=d.y;
        }
        else {
            if ( nrchars==4 )
                rest = "" + rest.charAt(0) + rest.charAt(2) + rest.charAt(1) + rest.charAt(3);
            v = fast_decode(rest);
            difx = ( v/yside[nrchars] );
            dify = ( v%yside[nrchars] );
        }

        dify = yside[nrchars]-1-dify;

        int cornery = rely + (dify*dividery);
        int cornerx = relx + (difx*dividerx);
        return add2res( cornery,cornerx, dividerx<<2,dividery,1);
    }


    private Point decode_starpipe(String input,int firstindex,int ccode) // returns Point.isUndefined() in case or error
    {
        int STORAGE_START=0;
        int difx,dify;
        int thiscodexlen = codexlen;

        int value = fast_decode(input); // decode top (before dot)
        value *= (961*31);
        Point triple = decode_triple( input.substring( input.length()-3 ) ); // decode bottom 3 chars

        int i;
        for(i=firstindex;;i++)
        {
            if (CodexLen(i)!=thiscodexlen) {
                return new Point(); // return undefined
            }
            if (i>firstindex) dataSetup(i);

            int H = ((maxy-miny+89)/90);
            int xdiv = x_divider(miny,maxy);
            int W = ( ( (maxx-minx)*4 + (xdiv-1) ) / xdiv );

            H = 176*( (H+176-1)/176 );
            W = 168*( (W+168-1)/168 );

            int product = (W/168)*(H/176)*961*31;

            int GOODROUNDER = codex>=23 ? (961*961*31) : (961*961);
            if ( pipetype==8 ) // *+
                product = ((STORAGE_START+product+GOODROUNDER-1)/GOODROUNDER) * GOODROUNDER - STORAGE_START;

            if ( value >= STORAGE_START && value < STORAGE_START + product ) // code belongs here?
            {
                int dividerx = ((maxx-minx+W-1)/W);
                int dividery = ((maxy-miny+H-1)/H);

                value -= STORAGE_START;
                value = ( value / (961*31) );
                // PIPELETTER DECODE
                int vx = (value / (H/176));
                vx = vx*168 + triple.x;
                int vy =           (value % (H/176))*176 + triple.y;

                int cornery = maxy - vy*dividery;
                int cornerx = minx + vx*dividerx;
/* @@@
					{
						int c1 = (zonedata==0) ? -1 : decode_chars[(int)input.charAt(input.length()-3)];
						Point zd=addzonedata(cornery+(triple.y-176)*dividery,cornerx-triple.x*dividerx,176*dividery,168*dividerx,input.substring(0,input.length()-3),ccode,c1,dividerx,dividery);
						cornery=zd.y;
						cornerx=zd.x;
					}
@@@ */

                Point retval = add2res(cornery,cornerx,dividerx<<2,dividery,-1);
                if ( retval.x<minx || retval.x>=maxx || retval.y<miny || retval.y>maxy )
                    retval.clear(); // return undefined
                return retval;
            }
            STORAGE_START += product;
        }
    } //decode_starpipe


    private Point decode_nameless(String result,int firstrec)
    {
        if ( codex==22 )
            result = result.substring(0,3)+result.substring(4);
        else
            result = result.substring(0,2)+result.substring(3);

        int A = count_city_coordinates_for_country(codex,firstrec,firstrec);
        if ( A<2 ) A=1; // paranoia

        int p = (31/A);
        int r = (31 % A);
        int v=0;
        int X=-1;
        boolean swapletters=false;

        if ( codex!=21 && A<=31 )
        {
            int offset = decode_chars[ (int)result.charAt(0) ];

            if ( offset < r*(p+1) )
            {
                X = ( offset / (p+1) );
            }
            else
            {
                swapletters=(p==1 && codex==22);
                X = r + ( (offset-(r*(p+1))) / p );
            }
        }
        else if ( codex!=21 && A<62 )
        {
            X = decode_chars[ (int)result.charAt(0) ];
            if ( X < (62-A) )
            {
                swapletters=(codex==22);
            }
            else
            {
                X = X+(X-(62-A));
            }
        }
        else // codex==21 || A>=62
        {
            int BASEPOWER = (codex==21) ? 961*961 : 961*961*31;
            int BASEPOWERA = (BASEPOWER/A);
            if (A==62) BASEPOWERA++; else BASEPOWERA = 961*(BASEPOWERA/961);

            // decode and determine x
            v = fast_decode(result);
            X  = (v/BASEPOWERA);
            v %= BASEPOWERA;
        }

        if (swapletters)
        {
            if ( ! isSpecialShape(firstrec+X) )
                result = result.substring(0,2) + result.charAt(3) + result.charAt(2) + result.charAt(4);
        }

        if ( codex!=21 && A<=31 )
        {
            v = fast_decode(result);
            if (X>0)
            {
                v -= ( (X*p + (X<r ? X : r)) * (961*961) );
            }
        }
        else if ( codex!=21 && A<62 )
        {
            v = fast_decode(result.substring(1));
            if ( X >= (62-A) )
                if ( v >= (16*961*31) )
                {
                    v -= (16*961*31);
                    X++;
                }
        }

        if (X>A) return new Point(); // return undefined (past end!)
        dataSetup(firstrec+X);

        int SIDE = smartdiv(firstrec+X);
        int xSIDE=SIDE;

        if ( isSpecialShape22 )
        {
            xSIDE *= SIDE;
            SIDE = 1+((maxy-miny)/90);
            xSIDE = (xSIDE/SIDE);
        }

        int dx,dy;
        if ( isSpecialShape22 ) {
            Point d = decode6(v,xSIDE,SIDE);
            dx = d.x;
            dy = SIDE-1-d.y;
        }
        else {
            dy = (v%SIDE);
            dx = (v/SIDE);
        }

        if ( dx >= xSIDE ) // else out-of-range!
            return new Point(); // return undefined (out of range!)

        int dividerx4 = x_divider(miny,maxy); // 4 times too large!
        int dividery = 90;

        int cornerx = minx + ((dx*dividerx4)/4); // FIRST multiply, THEN divide!
        int cornery = maxy - (dy*dividery);
        return add2res(cornery,cornerx, dividerx4,dividery,-1);
    } // decode_nameless


    public static String aeu_unpack(String str) // unpack encoded into all-digit (assume str already uppercase!), returns "" in case of error
    {
        boolean voweled=false;
        int lastpos = str.length()-1;
        int dotpos = str.indexOf('.');
        if (dotpos<2 || lastpos<dotpos+2 ) return ""; // Error: no dot, or less than 2 letters before dot, or less than 2 letters after dot

        if (str.charAt(0)=='A')
        {
            voweled=true;
            str=str.substring(1);
            dotpos--;
        }
        else
        {
            int v = str.charAt(lastpos-1);
            if (v=='A') v=0; else if (v=='E') v=34; else if (v=='U') v=68; else v=-1;
            if (v>=0)
            {
                char e = str.charAt(lastpos);
                if (e=='A') v+=31; else if (e=='E') v+=32; else if (e=='U') v+=33; else {
                    int ve = decode_chars[(int)str.charAt(lastpos)];
                    if (ve<0) return "";
                    v+=ve;
                }
                if (v>=100) return "";
                voweled=true;
                str = str.substring(0,lastpos-1) + encode_chars[(v/10)] + encode_chars[(v%10)];
            }
        }

        if (dotpos<2 || dotpos>5) return "";

        for (int v=0;v<=lastpos;v++) if (v!=dotpos)
            if (decode_chars[(int)str.charAt(v)]<0)
                return ""; // bad char!
            else if (voweled && decode_chars[(int)str.charAt(v)]>9)
                return ""; // nonodigit!

        return str;
    }


    public Point master_decode(String mapcode,int ccode) // in case of error, result.isDefined() is false
    {
        Point result = new Point();

        int minpos=mapcode.indexOf('-');
        if (minpos>0) {
            extrapostfix=mapcode.substring(minpos+1).trim();
            mapcode=mapcode.substring(0,minpos);
        } else {
            extrapostfix="";
        }

        mapcode=aeu_unpack(mapcode).trim();
        if (mapcode=="")
            return result; // failed to decode!


        int incodexlen = mapcode.length()-1;

        // *** long codes in states are handled by the country
        if (incodexlen>=9) ccode=ccode_earth; else
        {
            int parent=Ccoder.StateParent(ccode);
            if (incodexlen>=8 && parent==ccode_usa) ccode=parent;
            else if (incodexlen>=7 && parent==ccode_ind) ccode=parent;
            else if (incodexlen>=8 && parent==ccode_can) ccode=parent;
            else if (incodexlen>=8 && parent==ccode_aus) ccode=parent;
            else if (incodexlen>=7 && parent==ccode_mex) ccode=parent;
            else if (incodexlen>=8 && parent==ccode_bra) ccode=parent;
            else if (incodexlen>=8 && parent==ccode_chn) ccode=parent;
            else if (incodexlen>=8 && parent==ccode_rus) ccode=parent;
        }

        int from = dataFirstRecord(ccode);
        if (data_flags(from)==0) return new Point(); // this territory is not in the current data
        int upto = dataLastRecord(ccode);

        int incodexhi = mapcode.indexOf('.');

        for (int i=from; i<=upto; i++ ) {
            dataSetup(i);
            if ( pipetype==0 && !isnameless && codexlen==incodexlen && codexhi==incodexhi ) {
                result=decode_grid(mapcode,minx,miny,maxx,maxy,/*"",*/ccode,i);
                // RESTRICTUSELESS
                if ( isuseless && result.isDefined() ) {
                    boolean fitssomewhere=false;
                    int j;for (j=upto-1;j>=from;j--) { // look in previous rects
                        dataSetup(j); if (isuseless) continue;
                        if ( miny<=result.y && result.y<maxy && isInRange(result.x,minx,maxx) ) { fitssomewhere=true; break; }
                    }
                    if (!fitssomewhere) result.clear();
                }
                break;
            }
            else if ( pipetype==4 && codexlen+1==incodexlen && codexhi+1==incodexhi && pipeletter.charAt(0)==mapcode.charAt(0) ) {
                result=decode_grid(mapcode.substring(1),minx,miny,maxx,maxy,/*mapcode.substring(0,1),*/ccode,i);
                break;
            }
            else if (isnameless && ((codex==21 && incodexlen==4 && incodexhi==2) || (codex==22 && incodexlen==5 && incodexhi==3) || (codex==13 && incodexlen==5 && incodexhi==2)) ) {
                result=decode_nameless(mapcode,i);
                break;
            }
            else if ( pipetype>4 && incodexlen==incodexhi+3 && codexlen+1==incodexlen) {
                result=decode_starpipe(mapcode,i,ccode);
                break;
            }
        }

        if (result.isDefined()) {
            if ( result.x>180000000) result.x-=360000000;

            // LIMIT_TO_OUTRECT : make sure it fits the country
            if ( ccode!=ccode_earth )
            {
                minmaxSetup(upto); // find encompassing rect
                int xdiv8 = x_divider(miny,maxy)/4; // should be /8 but there's some extra margin
                if ( ! ( miny-45<=result.y && result.y<maxy+45 && isInRange(result.x,minx-xdiv8,maxx+xdiv8) ) ) { // no fit?
                    result.clear(); // decodes outside the official territory limit
                }
            }
        }

        return result;
    } // master_decode



} // class Mapcoder

