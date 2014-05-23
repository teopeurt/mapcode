package com.mapcoders;

/**
 * Created with IntelliJ IDEA.
 * User: don
 * Date: 23/05/2014
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
/*
	class MapcodeAlphabets
	----------------------

	static String to_ascii(String str);
		takes a unicode mapcode, and returns the "roman" or "ascii" equivalent, which can then be passed to master_decode()

	static String to_lan(String str,int lan,boolean asHTML);
		takes a mapcode string, and converts it to a unicode String in language 'lan'.
		(values for 'lan': 1=greek 2=Cyrillic 3=Hebrew 4=Hindi 5=Malai 6=Georgisch 7=Katakana 8=Thai 9=Lao 10=Armenian 11=Bengali 12=Gurmukhi 13=Tibetan)
		Pass asHTML=true to generate HTML-printable ascii instead of unicode.

*/

class MapcodeAlphabets {

    private static final int MAXLANS=14;
    private static final int[][] asc2lan = {
            {65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,48,49,50,51,52,53,54,55,56,57,-1}, // Roman
            {913,914,926,916,63,917,915,919,921,928,922,923,924,925,927,929,920,936,931,932,63,934,937,935,933,918,48,49,50,51,52,53,54,55,56,57,-1}, // Greek
            {1040,1042,1057,1044,1045,1046,1043,1053,1048,1055,1050,1051,1052,1047,1054,1056,1060,1071,1062,1058,1069,1063,1064,1061,1059,1041,48,49,50,51,52,53,54,55,56,57,-1}, // Cyrillic
            {1488,1489,1490,1491,1507,1492,1494,1495,1493,1496,1497,1498,1499,1500,1505,1501,1502,1504,1506,1508,1509,1510,1511,1512,1513,1514,48,49,50,51,52,53,54,55,56,57,-1}, // Hebrew
            {2309,2325,2327,2328,2319,2330,2332,2335,63,2336,2339,2340,2342,2343,63,2344,2346,2349,2350,2352,2347,2354,2357,2360,2361,2337,2406,2407,2408,2409,2410,2411,2412,2413,2414,2415,-1}, // Hindi
            {3346,3349,3350,3351,3339,3354,3356,3359,3335,3361,3364,3365,3366,3367,3360,3368,3374,3376,3377,3378,3337,3380,3381,3382,3384,3385,3430,3431,3432,3433,3434,3435,3436,3437,3438,3439,-1}, // Malai
            {4256,4257,4259,4262,4260,4265,4267,4268,4275,4270,4272,4273,4274,4276,4269,4277,4278,4279,4280,4281,4264,4282,4283,4285,4286,4287,48,49,50,51,52,53,54,55,56,57,-1}, // Georgisch
            {12450,12459,12461,12463,12458,12465,12467,12469,12452,12473,12481,12488,12490,12492,12454,12498,12501,12504,12507,12513,12456,12514,12520,12521,12525,12530,48,49,50,51,52,53,54,55,56,57,-1}, // Katakana
            {3632,3585,3586,3588,3634,3591,3592,3593,3633,3594,3601,3604,3606,3607,3597,3608,3610,3612,3617,3619,3628,3621,3623,3629,3630,3631,3664,3665,3666,3667,3668,3669,3670,3671,3672,3673,-1}, // Thai
            {3760,3713,3714,3716,3779,3719,3720,3722,3780,3725,3732,3735,3737,3738,3782,3740,3742,3745,3746,3747,3773,3751,3754,3755,3757,3759,48,49,50,51,52,53,54,55,56,57,-1}, // Lao
            {1366,1330,1331,1332,1333,1336,1337,1338,1339,1341,1343,1344,1345,1347,1365,1351,1352,1354,1357,1358,1349,1359,1360,1361,1362,1363,48,49,50,51,52,53,54,55,56,57,-1}, // Armenian
            {2437,2444,2453,2454,2447,2455,2457,2458,63,2461,2464,2465,2466,2467,63,2468,2469,2470,2472,2474,2451,2476,2477,2479,2482,2489,2534,2535,2536,2537,2538,2539,2540,2541,2542,2543,-1}, // Bengali
            {2565,2581,2583,2584,2575,2586,2588,2591,63,2592,2595,2596,2598,2599,63,2600,2602,2605,2606,2608,2603,2610,2613,2616,2617,2593,2662,2663,2664,2665,2666,2667,2668,2669,2670,2671,-1}, // Gurmukhi
            {3928,3904,3905,3906,3940,3908,3909,3910,63,3911,3914,3916,3918,3919,63,3921,3923,3924,3926,3934,3941,3935,3937,3938,3939,3942,3872,3873,3874,3875,3876,3877,3878,3879,3880,3881,-1}, // Tibetan
    };

    // *UI*
    private static final String[] lannam = {
            "Roman",
            "Greek",
            "Cyrillic",
            "Hebrew",
            "Hindi",
            "Malai",
            "Georgisch",
            "Katakana",
            "Thai",
            "Lao",
            "Armenian",
            "Bengali",
            "Gurmukhi",
            "Tibetan"
    };

    // *UI*
    private static final String[] lanlannam = {
            "Roman",
            "&#949;&#955;&#955;&#951;&#957;&#953;&#954;&#940;",
            "&#1082;&#1080;&#1088;&#1080;&#1083;&#1083;&#1080;&#1094;&#1072;",
            "&#1506;&#1460;&#1489;&#1456;&#1512;&#1460;&#1497;&#1514;",
            "&#2361;&#2367;&#2306;&#2342;&#2368;",
            "Melayu",
            "&#4325;&#4304;&#4320;&#4311;&#4323;&#4314;&#4312;",
            "&#12459;&#12479;&#12459;&#12490;",
            "&#3616;&#3634;&#3625;&#3634;&#3652;&#3607;&#3618;",
            "&#3742;&#3762;&#3754;&#3762;&#3749;&#3762;&#3751;",
            "&#1392;&#1377;&#1397;&#1381;&#1408;&#1381;&#1398;",
            "&#2476;&#2494;&#2434;&#2482;&#2494;",
            "&#2583;&#2625;&#2608;&#2606;&#2625;&#2582;&#2624;",
            "&#3921;&#3926;&#3956;&#3851;&#3909;&#3923;&#3851;"
    };


    public static String lanname_english(int i) { return lannam[i]; }
    public static String lanname_local(int i) { return lanlannam[i]; }


    /// substitute characters in str with characters form the specified language (pass asHTML=1 to explicitly HTML-encode characters)
    public static String to_lan(String str,int lan,boolean asHTML)
    {
        if (lan==0)
            return str;

        String result="";

        // unpack for languages that do not support E and U
        if (asc2lan[lan][4]==63) { // is there no equivalent for the letter E in this language?
            if ( str.indexOf('E')>=0 || str.indexOf('U')>=0) {
                String str2=Mapcoder.aeu_unpack(str);
                if (str!=str2) {
                    int dc=str2.indexOf(".");
                    str='A'+str2;
                }
            }
        }

        // substitute
        {
            for (int i=0;i<str.length();i++)
            {
                int c=(int)str.charAt(i);
                if (c>=65 && c<=93)
                    c=asc2lan[lan][c-65];
                else if (c>=48 && c<=57)
                    c=asc2lan[lan][c+26-48];

                if (asHTML)
                    result+="&#"+Integer.toString(c)+";";
                else
                    result+=((char)c);
            }
        }
        return result;
    }


    public static String to_ascii(String str)
    {
        final String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result="";
        int len=str.length();
        for(int i=0;i<len;i++) {
            int c = (int)str.charAt(i);
            if (c>0 && c<127) result+=str.charAt(i); else {
                boolean found=false;
                for (int lan=0;lan<MAXLANS;lan++) {
                    for (int j=0;asc2lan[lan][j]>=0;j++) {
                        if (c==asc2lan[lan][j]) {
                            result+=letters.charAt(j);
                            found=true;
                            break;
                        }
                    }
                    if (found) break;
                }
                if (!found) result+="?";
            }
        }
        return result;
    }

} // MapcodeAlphabets


