# PChouse Reports API

A Rest API to generate reports for jasper report files, using the Rebelo Reports core.  
Create reports to a file or printer 
    
# Rebelo Reports

Rebelo Reports is a middleware to use the Jasper Reports Framework in the cases that you can not use the Jasper Reports embedded in you software,in cases that you software is not java or there are incompatibility of licences.

Project in:  
https://github.com/joaomfrebelo/reports_core

## To execute 
Listen on default port 4999 and allow only request from localhost 
```
java -jar api-x.x.x.jar
```

Listen on different port
```
java -jar -Dserver.port=9999 api-x.x.x.jar
```

Authorize request from listed ip

```
java -jar -Dclient.allowIps="127.0.0.1,10.0.0.1" api-x.x.x.jar
```
Set a printer properties file, create a file named printer.properties
```
java -jar -Dapp.home="/path/to/file/directory" api-x.x.x.jar
```
  
## Features  
### Export to:  
- PDF  
- Digital Sign PDF
- Csv
- Docx
- Html
- Json
- Ods  
- Odt  
- Pptx  
- Rtf  
- Text  
- Xls  
- Xlsx  
- Xml  
- To printer
- Cut paper on ticket printer
- Open cash drawer

### Json Request

 - See the reportRequest.json file in resources test folder

## License

Copyright (C) 2019  Reflexão, Estudos e Sistemas Informáticos, Lda

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

## License issues

The use of PChouse Reports api non AGPL software and with Commercial software:

Because  PChouse Reports uses AGPL license it’s important to keep the AGPL license file around. In my opinion your software that is going to invoke PChouse Reports doesn’t have to be licensed under AGPL as the Rebelo Reports if is being  used via its modules as an external executable Midleware and not as part of your project and your source code, compile, install and use as two separated software.

How ever this only reflects my opinion about the compatibilities of licences and the use of the software under AGPL and other licences included commercial ones as described above, is your responsibility to verify if the legality of the compatibility and uses.
