import random
import DataSet

''' Class for writing ARFF files. '''
def t(var):
        ''' Returns variable type name (e.g. 'str', 'int') '''
        return type(var).__name__

class ARFF:
    
    def __init__(self, relation, attrs, data, percent=1.0):
        ''' @param relation: dataset name 
            @param attrs: attributes (features) for dataset header
            @param data: data in numpy matrix
            @param percent: (optional) percent of data to randomly choose from. 
                            1.0 is 100%.
        '''
        
        self.attrs = attrs
        self.data = data
        self.percent = percent
        self.dataSetClass = DataSet.DataSet()
        self.arff = self.makeHeader(relation, self.attrs)
        self.arff += "\n"+self.makeData(self.data, self.percent)
        #print self.arff
        
    def makeHeader(self, relation, attributes):
        ''' Makes ARFF header section without comments.
            @param relation: <str> name of dataset 
            @param attributes: <list> features names (and their type/values)
            @return <str>'''
        
        header = "@RELATION "+relation+"\n\n"
        
        for i in range(len(attributes)):
            attr = attributes[i]
            attributeName = attr[0]
            attributeType = attr[1]
    
            header += "@ATTRIBUTE "+str(attributeName)+" "
            
            if t(attributeType) == "str":
                
                # it's NUMERIC, DATE, or STRING
                header += attributeType
                
                if attributeType == "DATE":
                    header += ' "'+attr[2]+'"'
            
            else:
                # It's a nominal-specification
                header += "{"+",".join(map(str,attributeType))+"}"
                
            header += "\n"
        return header
    
    def makeData(self, data, percent):
        ''' Write DATA section of ARFF file. '''
        ##### TODO: percent
        dataString = "@DATA"
        dataLen = float(len(data))
        dataLeft = data
        
        for i in range(int(dataLen)):
            
            # Stop once percent is reached
            if ((i+1)/dataLen) > percent:
                break
            
            # Get random row
            randRowIdx = random.randint(0, len(dataLeft)-1)
            line = dataLeft.pop(randRowIdx)
            
            toPrint = []
            for idx,pt in enumerate(line):
                if t(pt) == "int":
                    pt = str(pt)
                elif self.attrs[idx][1] == "STRING":
                    pt = '"'+str(pt)+'"'
                toPrint.append(pt)
            dataString += "\n"+",".join(map(str,toPrint))
            
        return dataString
    
    def formatDate(self, datetime):
        ''' Formats date in expected ARFF format (ISO-8601)
            @param datetime: <list> in [yr, mon, day, hr, min, sec] format
            @return: <str>'''
        
        # Where to store validated date parts
        c = []
        
        # Check to make sure each parameter is valid
        for datePart in datetime:
            if t(datePart) != "int":
                try:
                    datePart = int(datePart)
                except:
                    print "Datetime part is incorrect (expected int)"
                    return
            
            if datePart < 0:
                print "Invalid datetime (expected 0 or positive integer)"
                return
                
            if datePart < 10:
                datePart = "0"+str(datePart)
            else:
                datePart = str(datePart)
            
            c.append(datePart)
        
        datetime = c[0]+"-"+c[1]+"-"+c[2]+" "+c[3]+":"+c[4]+":"+c[5]
        return datetime
    
    def toFile(self, filename):
        ''' Writes ARFF file to disk.
            @param filename: path to ARFF file to create
            @return: None
        '''
        file = open(filename, "w")
        file.write(self.arff)
        file.close()