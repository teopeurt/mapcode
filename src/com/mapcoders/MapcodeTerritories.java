package com.mapcoders;

/**
 * Created with IntelliJ IDEA.
 * User: don
 * Date: 23/05/2014
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;

/*
	class to access territory names
	-------------------------------
	MapcodeTerritories.fullname(ccode,false)
		gets the human-readable english name of territory 'ccode'
		(where ccode is a valid territory code, see Ccoder)
*/
class MapcodeTerritories {

    /// return name of iso (optional keepindex for bracketed aliases); returns ??? in case of error
    public static String fullname(int ccode,boolean keepindex)
    {
        int MAX_CCODE=isofullname.length-1;
        if (ccode<0 || ccode>=MAX_CCODE) return "???";
        if ( ! keepindex ) {
            final int idx = isofullname[ccode].indexOf(" (");
            if (idx>0)
                return isofullname[ccode].substring(0,idx);
        }
        return isofullname[ccode];
    }

    private static final String [] isofullname = {

            "Vatican City (Holy See)","Monaco","Gibraltar","Tokelau",
            "Cocos Islands (Keeling Islands)","Saint-Barthelemy","Nauru","Tuvalu",
            "Macau","Sint Maarten","Saint-Martin","Norfolk and Philip Island (Philip Island)",
            "Pitcairn Islands","Bouvet Island","Bermuda","British Indian Ocean Territory",
            "San Marino","Guernsey","Anguilla","Montserrat",
            "Jersey","Christmas Island","Wallis and Futuna (Futuna)","British Virgin Islands (Virgin Islands, British)",
            "Liechtenstein","Aruba","Marshall Islands","American Samoa (Samoa, American)",
            "Cook islands","Saint Pierre and Miquelon (Miquelon)","Niue","Saint Kitts and Nevis (Nevis)",
            "Cayman islands","Bonaire, St Eustasuis and Saba (Saba) (St Eustasius)","Maldives","Saint Helena, Ascension and Tristan da Cunha (Ascension) (Tristan da Cunha)",
            "Malta","Grenada","US Virgin Islands (Virgin Islands, US)","Mayotte",
            "Svalbard and Jan Mayen (Jan Mayen)","Saint Vincent and the Grenadines (Grenadines)","Heard Island and McDonald Islands (McDonald Islands)","Barbados",
            "Antigua and Barbuda (Barbuda)","Curacao","Seychelles","Palau",
            "Northern Mariana Islands","Andorra","Guam","Isle of Man",
            "Saint Lucia","Micronesia (Federated States of Micronesia)","Singapore","Tonga",
            "Dominica","Bahrain","Kiribati","Turks and Caicos Islands (Caicos Islands)",
            "Sao Tome and Principe (Principe)","Hong Kong","Martinique","Faroe Islands",
            "Guadeloupe","Comoros","Mauritius","Reunion",
            "Luxembourg","Samoa","South Georgia and the South Sandwich Islands (South Sandwich Islands)","French Polynesia",
            "Cape Verde","Trinidad and Tobago (Tobago)","Brunei","French Southern and Antarctic Lands",
            "Puerto Rico","Cyprus","Lebanon","Jamaica",
            "Gambia","Qatar","Falkland Islands","Vanuatu",
            "Montenegro","Bahamas","East Timor","Swaziland",
            "Kuwait","Fiji Islands","New Caledonia","Slovenia",
            "Israel","Palestinian territory","El Salvador","Belize",
            "Djibouti","Macedonia","Rwanda","Haiti",
            "Burundi","Equatorial Guinea","Albania","Solomon Islands",
            "Armenia","Lesotho","Belgium","Moldova",
            "Guinea-Bissau","Taiwan","Bhutan","Switzerland",
            "Netherlands","Denmark","Estonia","Dominican Republic",
            "Slovakia","Costa Rica","Bosnia and Herzegovina","Croatia",
            "Togo","Latvia","Lithuania","Sri Lanka",
            "Georgia","Ireland","Sierra Leone","Panama",
            "Czech Republic","French Guiana","United Arab Emirates","Austria",
            "Azerbaijan","Serbia","Jordan","Portugal",
            "Hungary","South Korea","Iceland","Guatemala",
            "Cuba","Bulgaria","Liberia","Honduras",
            "Benin","Eritrea","Malawi","North Korea",
            "Nicaragua","Greece","Tajikistan","Bangladesh",
            "Nepal","Tunisia","Suriname","Uruguay",
            "Cambodia","Syria","Senegal","Kyrgyzstan",
            "Belarus","Guyana","Laos","Romania",
            "Ghana","Uganda","United Kingdom (Scotland) (Great Britain) (Northern Ireland) (Ireland, Northern)","Guinea",
            "Ecuador","Western Sahara (Sahrawi)","Gabon","New Zealand",
            "Burkina Faso","Philippines","Italy","Oman",
            "Poland","Ivory Coast","Norway","Malaysia",
            "Vietnam","Finland","Congo-Brazzaville","Germany",
            "Japan","Zimbabwe","Paraguay","Iraq",
            "Morocco","Uzbekistan","Sweden","Papua New Guinea",
            "Cameroon","Turkmenistan","Spain","Thailand",
            "Yemen","France","Aaland Islands","Kenya",
            "Botswana","Madagascar","Ukraine","South Sudan",
            "Central African Republic","Somalia","Afghanistan","Myanmar (Burma)",
            "Zambia","Chile","Turkey","Pakistan",
            "Mozambique","Namibia","Venezuela","Nigeria",
            "Tanzania","Egypt","Mauritania","Bolivia",
            "Ethiopia","Colombia","South Africa","Mali",
            "Angola","Niger","Chad","Peru",
            "Mongolia","Iran","Libya","Sudan",
            "Indonesia","Federal District","Tlaxcala","Morelos",
            "Aguascalientes","Colima","Queretaro","Hidalgo",
            "Mexico State","Tabasco","Nayarit","Guanajuato",
            "Puebla","Yucatan","Quintana Roo","Sinaloa",
            "Campeche","Michoacan","San Luis Potosi","Guerrero",
            "Nuevo Leon","Baja California","Veracruz","Chiapas",
            "Baja California Sur","Zacatecas","Jalisco","Tamaulipas",
            "Oaxaca","Durango","Coahuila","Sonora",
            "Chihuahua","Greenland","Saudi Arabia","Congo-Kinshasa",
            "Algeria","Kazakhstan","Argentina","Daman and Diu",
            "Dadra and Nagar Haveli","Chandigarh","Andaman and Nicobar","Lakshadweep",
            "Delhi","Meghalaya","Nagaland","Manipur",
            "Tripura","Mizoram","Sikkim","Punjab",
            "Haryana","Arunachal Pradesh","Assam","Bihar",
            "Uttarakhand","Goa","Kerala","Tamil Nuda",
            "Himachal Pradesh","Jammu and Kashmir","Chhattisgarh","Jharkhand",
            "Karnataka","Rajasthan","Odisha (Orissa)","Gujarat",
            "West Bengal","Madhya Pradesh","Andhra Pradesh","Maharashtra",
            "Uttar Pradesh","Puducherry","New South Wales","Australian Capital Territory",
            "Jervis Bay Territory","Northern Territory","South Australia","Tasmania",
            "Victoria","Western Australia","Queensland","Distrito Federal",
            "Sergipe","Alagoas","Rio de Janeiro","Espirito Santo",
            "Rio Grande do Norte","Paraiba","Santa Catarina","Pernambuco",
            "Amapa","Ceara","Acre","Parana",
            "Roraima","Rondonia","Sao Paulo","Piaui",
            "Tocantins","Rio Grande do Sul","Maranhao","Goias",
            "Mato Grosso do Sul","Bahia","Minas Gerais","Mato Grosso",
            "Para","Amazonas","District of Columbia","Rhode Island",
            "Delaware","Connecticut","New Jersey","New Hampshire",
            "Vermont","Massachusetts","Hawaii","Maryland",
            "West Virginia","South Carolina","Maine","Indiana",
            "Kentucky","Tennessee","Virginia","Ohio",
            "Pennsylvania","Mississippi","Louisiana","Alabama",
            "Arkansas","North Carolina","New York","Iowa",
            "Illinois","Georgia","Wisconsin","Florida",
            "Missouri","Oklahoma","North Dakota","Washington",
            "South Dakota","Nebraska","Kansas","Idaho",
            "Utah","Minnesota","Michigan","Wyoming",
            "Oregon","Colorado","Nevada","Arizona",
            "New Mexico","Montana","California","Texas",
            "Alaska","British Columbia","Alberta","Ontario",
            "Quebec","Saskatchewan","Manitoba","Newfoundland",
            "New Brunswick","Nova Scotia","Prince Edward Island","Yukon",
            "Northwest Territories","Nunavut","India","Australia",
            "Brazil","USA (United States of America) (America)","Mexico","Moscow",
            "Saint Petersburg","Kaliningrad Oblast","Ingushetia Republic","Adygea Republic",
            "North Ossetia-Alania Republic","Kabardino-Balkar Republic","Karachay-Cherkess Republic","Chechen Republic",
            "Chuvash Republic","Ivanovo Oblast","Lipetsk Oblast","Oryol Oblast",
            "Tula Oblast","Belgorod Oblast","Vladimir Oblast","Kursk Oblast",
            "Kaluga Oblast","Tambov Oblast","Bryansk Oblast","Yaroslavl Oblast",
            "Ryazan Oblast","Astrakhan Oblast","Moscow Oblast","Smolensk Oblast",
            "Dagestan Republic","Voronezh Oblast","Novgorod Oblast","Pskov Oblast",
            "Kostroma Oblast","Stavropol Krai","Krasnodar Krai","Kalmykia Republic",
            "Tver Oblast","Leningrad Oblast","Rostov Oblast","Volgograd Oblast",
            "Vologda Oblast","Murmansk Oblast","Karelia Republic","Nenets Autonomous Okrug",
            "Komi Republic","Arkhangelsk Oblast","Mordovia Republic","Nizhny Novgorod Oblast",
            "Penza Oblast","Kirov Oblast","Mari El Republic","Orenburg Oblast",
            "Ulyanovsk Oblast","Perm Krai","Bashkortostan Republic","Udmurt Republic",
            "Tatarstan Republic","Samara Oblast","Saratov Oblast","Yamalo-Nenets",
            "Khanty-Mansi","Sverdlovsk Oblast","Tyumen Oblast","Kurgan Oblast",
            "Chelyabinsk Oblast","Buryatia Republic","Zabaykalsky Krai","Irkutsk Oblast",
            "Novosibirsk Oblast","Tomsk Oblast","Omsk Oblast","Khakassia Republic",
            "Kemerovo Oblast","Altai Republic","Altai Krai","Tuva Republic",
            "Krasnoyarsk Krai","Magadan Oblast","Chukotka Okrug","Kamchatka Krai",
            "Sakhalin Oblast","Primorsky Krai","Jewish Autonomous Oblast","Khabarovsk Krai",
            "Amur Oblast","Sakha Republic (Yakutia Republic)","Canada","Russia",
            "Shanghai","Tianjin","Beijing","Hainan",
            "Ningxia Hui","Chongqing","Zhejiang","Jiangsu",
            "Fujian","Anhui","Liaoning","Shandong",
            "Shanxi","Jiangxi","Henan","Guizhou",
            "Guangdong","Hubei","Jilin","Hebei",
            "Shaanxi","Nei Mongol (Inner Mongolia)","Heilongjiang","Hunan",
            "Guangxi Zhuang","Sichuan","Yunnan","Xizang (Tibet)",
            "Gansu","Qinghai","Xinjiang Uyghur","China",
            "United States Minor Outlying Islands","Clipperton Island","Macquarie Island","Ross Dependency",
            "Adelie Land","Australian Antarctic Territory","Queen Maud Land","British Antarctic Territory",
            "Chile Antartica","Argentine Antarctica","Peter 1 Island","Antarctica",
            "International (Worldwide) (Earth)","?"	};
}
