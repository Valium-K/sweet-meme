package dev.valium.sweetmeme.infra.processor;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Code2State {
    private final String stateString =
            "Afghanistan/AF/" +
            "Åland Islands/AX/" +
            "Albania/AL/" +
            "Algeria/DZ/" +
            "American Samoa/AS/" +
            "Andorra/AD/" +
            "Angola/AO/" +
            "Anguilla/AI/" +
            "Antarctica/AQ/" +
            "Antigua and Barbuda/AG/" +
            "Argentina/AR/" +
            "Armenia/AM/" +
            "Aruba/AW/" +
            "Australia/AU/" +
            "Austria/AT/" +
            "Azerbaijan/AZ/" +
            "Bahamas/BS/" +
            "Bahrain/BH/" +
            "Bangladesh/BD/" +
            "Barbados/BB/" +
            "Belarus/BY/" +
            "Belgium/BE/" +
            "Belize/BZ/" +
            "Benin/BJ/" +
            "Bermuda/BM/" +
            "Bhutan/BT/" +
            "Bolivia/BO/" +
            "Bosnia and Herzegovina/BA/" +
            "Botswana/BW/" +
            "Bouvet Island/BV/" +
            "Brazil/BR/" +
            "British Indian Ocean Territory/IO/" +
            "Brunei Darussalam/BN/" +
            "Bulgaria/BG/" +
            "Burkina Faso/BF/" +
            "Burundi/BI/" +
            "Cambodia/KH/" +
            "Cameroon/CM/" +
            "Canada/CA/" +
            "Cape Verde/CV/" +
            "Cayman Islands/KY/" +
            "Central African Republic/CF/" +
            "Chad/TD/" +
            "Chile/CL/" +
            "China/CN/" +
            "Christmas Island/CX/" +
            "Cocos (Keeling) Islands/CC/" +
            "Colombia/CO/" +
            "Comoros/KM/" +
            "Congo/CG/" +
            "Congo, The Democratic Republic of the/CD/" +
            "Cook Islands/CK/" +
            "Costa Rica/CR/" +
            "Cote D'Ivoire/CI/" +
            "Croatia/HR/" +
            "Cuba/CU/" +
            "Cyprus/CY/" +
            "Czech Republic/CZ/" +
            "Denmark/DK/" +
            "Djibouti/DJ/" +
            "Dominica/DM/" +
            "Dominican Republic/DO/" +
            "Ecuador/EC/" +
            "Egypt/EG/" +
            "El Salvador/SV/" +
            "Equatorial Guinea/GQ/" +
            "Eritrea/ER/" +
            "Estonia/EE/" +
            "Ethiopia/ET/" +
            "Falkland Islands (Malvinas)/FK/" +
            "Faroe Islands/FO/" +
            "Fiji/FJ/" +
            "Finland/FI/" +
            "France/FR/" +
            "French Guiana/GF/" +
            "French Polynesia/PF/" +
            "French Southern Territories/TF/" +
            "Gabon/GA/" +
            "Gambia/GM/" +
            "Georgia/GE/" +
            "Germany/DE/" +
            "Ghana/GH/" +
            "Gibraltar/GI/" +
            "Greece/GR/" +
            "Greenland/GL/" +
            "Grenada/GD/" +
            "Guadeloupe/GP/" +
            "Guam/GU/" +
            "Guatemala/GT/" +
            "Guernsey/GG/" +
            "Guinea/GN/" +
            "Guinea-Bissau/GW/" +
            "Guyana/GY/" +
            "Haiti/HT/" +
            "Heard Island and Mcdonald Islands/HM/" +
            "Holy See (Vatican City State)/VA/" +
            "Honduras/HN/" +
            "Hong Kong/HK/" +
            "Hungary/HU/" +
            "Iceland/IS/" +
            "India/IN/" +
            "Indonesia/ID/" +
            "Iran, Islamic Republic Of/IR/" +
            "Iraq/IQ/" +
            "Ireland/IE/" +
            "Isle of Man/IM/" +
            "Israel/IL/" +
            "Italy/IT/" +
            "Jamaica/JM/" +
            "Japan/JP/" +
            "Jersey/JE/" +
            "Jordan/JO/" +
            "Kazakhstan/KZ/" +
            "Kenya/KE/" +
            "Kiribati/KI/" +
            "Korea, Democratic People'S Republic of/KP/" +
            "Korea, Republic of/KR/" +
            "Kuwait/KW/" +
            "Kyrgyzstan/KG/" +
            "Lao People'S Democratic Republic/LA/" +
            "Latvia/LV/" +
            "Lebanon/LB/" +
            "Lesotho/LS/" +
            "Liberia/LR/" +
            "Libyan Arab Jamahiriya/LY/" +
            "Liechtenstein/LI/" +
            "Lithuania/LT/" +
            "Luxembourg/LU/" +
            "Macao/MO/" +
            "Macedonia, The Former Yugoslav Republic of/MK/" +
            "Madagascar/MG/" +
            "Malawi/MW/" +
            "Malaysia/MY/" +
            "Maldives/MV/" +
            "Mali/ML/" +
            "Malta/MT/" +
            "Marshall Islands/MH/" +
            "Martinique/MQ/" +
            "Mauritania/MR/" +
            "Mauritius/MU/" +
            "Mayotte/YT/" +
            "Mexico/MX/" +
            "Micronesia, Federated States of/FM/" +
            "Moldova, Republic of/MD/" +
            "Monaco/MC/" +
            "Mongolia/MN/" +
            "Montserrat/MS/" +
            "Morocco/MA/" +
            "Mozambique/MZ/" +
            "Myanmar/MM/" +
            "Namibia/NA/" +
            "Nauru/NR/" +
            "Nepal/NP/" +
            "Netherlands/NL/" +
            "Netherlands Antilles/AN/" +
            "New Caledonia/NC/" +
            "New Zealand/NZ/" +
            "Nicaragua/NI/" +
            "Niger/NE/" +
            "Nigeria/NG/" +
            "Niue/NU/" +
            "Norfolk Island/NF/" +
            "Northern Mariana Islands/MP/" +
            "Norway/NO/" +
            "Oman/OM/" +
            "Pakistan/PK/" +
            "Palau/PW/" +
            "Palestinian Territory, Occupied/PS/" +
            "Panama/PA/" +
            "Papua New Guinea/PG/" +
            "Paraguay/PY/" +
            "Peru/PE/" +
            "Philippines/PH/" +
            "Pitcairn/PN/" +
            "Poland/PL/" +
            "Portugal/PT/" +
            "Puerto Rico/PR/" +
            "Qatar/QA/" +
            "Reunion/RE/" +
            "Romania/RO/" +
            "Russian Federation/RU/" +
            "RWANDA/RW/" +
            "Saint Helena/SH/" +
            "Saint Kitts and Nevis/KN/" +
            "Saint Lucia/LC/" +
            "Saint Pierre and Miquelon/PM/" +
            "Saint Vincent and the Grenadines/VC/" +
            "Samoa/WS/" +
            "San Marino/SM/" +
            "Sao Tome and Principe/ST/" +
            "Saudi Arabia/SA/" +
            "Senegal/SN/" +
            "Serbia and Montenegro/CS/" +
            "Seychelles/SC/" +
            "Sierra Leone/SL/" +
            "Singapore/SG/" +
            "Slovakia/SK/" +
            "Slovenia/SI/" +
            "Solomon Islands/SB/" +
            "Somalia/SO/" +
            "South Africa/ZA/" +
            "South Georgia and the South Sandwich Islands/GS/" +
            "Spain/ES/" +
            "Sri Lanka/LK/" +
            "Sudan/SD/" +
            "Suriname/SR/" +
            "Svalbard and Jan Mayen/SJ/" +
            "Swaziland/SZ/" +
            "Sweden/SE/" +
            "Switzerland/CH/" +
            "Syrian Arab Republic/SY/" +
            "Taiwan, Province of China/TW/" +
            "Tajikistan/TJ/" +
            "Tanzania, United Republic of/TZ/" +
            "Thailand/TH/" +
            "Timor-Leste/TL/" +
            "Togo/TG/" +
            "Tokelau/TK/" +
            "Tonga/TO/" +
            "Trinidad and Tobago/TT/" +
            "Tunisia/TN/" +
            "Turkey/TR/" +
            "Turkmenistan/TM/" +
            "Turks and Caicos Islands/TC/" +
            "Tuvalu/TV/" +
            "Uganda/UG/" +
            "Ukraine/UA/" +
            "United Arab Emirates/AE/" +
            "United Kingdom/GB/" +
            "United States/US/" +
            "United States Minor Outlying Islands/UM/" +
            "Uruguay/UY/" +
            "Uzbekistan/UZ/" +
            "Vanuatu/VU/" +
            "Venezuela/VE/" +
            "Viet Nam/VN/" +
            "Virgin Islands, British/VG/" +
            "Virgin Islands, U.S./VI/" +
            "Wallis and Futuna/WF/" +
            "Western Sahara/EH/" +
            "Yemen/YE/" +
            "Zambia/ZM/" +
            "Zimbabwe/ZW";
    private Map<String, String> codeMap = new HashMap<>(244);
    private List<String> collect = Arrays.stream(stateString.split("/")).collect(Collectors.toList());
    private boolean isInited = false;

    private void initCodeMap() {
        for (int i = 0; i < collect.size(); i += 2) {
            codeMap.put(collect.get(i+1), collect.get(i));
        }
        this.isInited = true;
    }

    public String findStateByCode(String code) {
        if(!isInited) initCodeMap();
        if(code == null) return null;

        return codeMap.get(code.toUpperCase());
    }

    /**
     * return: 에러일 경우 null, 빈 form일 경우 ""
     * @param json
     * @return
     */
    public String json2Code(String json) {
        try {
            if("".equals(json)) {
                log.info("profile - 국가: empty form.");
                return json;
            }

            String code = json.split("\"")[7].toLowerCase();

            if(findStateByCode(code) == null) {
                log.error("profile - 국가: 등록된 국가가 아닙니다.");
                return null;
            }

            return code;

        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("profile - 국가: 올바른 json 형식이 아닙니다.");
            return null;
        }
    }
}