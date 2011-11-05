import re
'''
Class for extracting and "cleaning" CS Log Data.
'''

class CSLogDataClass:
    def __init__(self,openFile,debugMode=False):
        ''' Params: 
                fileName <string> - path to cs log file
                debugMode <bool> - whether or not to display debug messages
        ''' 
        self.dbg = debugMode
        self.debug("Loading data...")
        self.data = openFile
        self.debug("Data successfully loaded.")
        self.lines = []

    def debug(self,str):
        ''' Prints debug messages '''
        if self.dbg:
            print str

    def extractData(self,line):
        ''' Extracts data from a single line <string> 
            Returns <list> in the following format: 
            
                [ipLocation, dateTime, referrer, statusCode, pageSize, 
                 httpVersion, accessedPage, userAgent] '''
            
        months = {"Jan":"01","Feb":"02","Mar":"03","Apr":"04","May":"05",
                  "Jun":"06","Jul":"07","Aug":"08","Sep":"09","Oct":"10",
                  "Nov":"11","Dec":"12"}
        
        data = []
        
        # IP address
        extract1 = line.split(" ",1)
        ip = extract1[0].lstrip().rsplit(".",2)[0]
        
        # Check if it's a Colby IP
        if ip == "137.146":
            ip = 1 # on campus
        else:
            ip = 0 # off campus
        data.append(ip)
        
        extract2 = re.split("^[^\[]+",extract1[1])[1].strip()
        extract2 = re.split('\s"',extract2)
        
        # Datetime
        date = re.split('[\s\[\]]',extract2[0])
        utcOffset = date[2] # We don't need this
        date = date[1].split(":",1)
        dmy = date[0].split("/")
        time = date[1].split(":")
        time = ''.join(map(str,time))
        date = dmy[2] + months[dmy[1]] + dmy[0]
        data.append(int(date))
        data.append(int(time))
        
        # Referrer
        ref = extract2[2][:-1]
        if ref == "-":
            ref = 0 # no referrer
        else:
            ref = ref.split("/",3)
            if len(ref) == 3:
                if ref[2] == "cs.colby.edu" or ref[2] == "www.cs.colby.edu" \
                or ref[2] == "cs":
                    ref = 1 # from colby
                else:
                    ref = 2
            else:
                ref = 2 # from outside (e.g. google)
        data.append(ref)
        
        get = extract2[1].split()
        get[2] = get[2][:-1]
        
        # HTTP status code
        data.append(int(get[3]))
        
        # Page size (bytes)
        # Note: if 3XX status, pageSize = 0
        pageSize = get[4]
        if pageSize == "-":
            pageSize = 0
        data.append(int(pageSize)) 
        
        # HTTP version : v1.0 -> 10, v1.1 -> 11
        httpVersion = get[2].split("/")[1].split(".")
        data.append(int(httpVersion[0]+httpVersion[1])) 
        
        # Accessed page
        data.append(get[1])
        
        # User-agent string
        data.append(extract2[3][:-1])
        
        return data
    
    def cleanData(self):
        ''' Go through line by line and extract data.
            Data is stored inside self["lines"]. '''

        self.debug("Starting data extraction...")
        
        # Loop through data and eXtract
        for line in self.data:
            self.lines.append(self.extractData(line))
            
        self.debug("Finished extracting data.")