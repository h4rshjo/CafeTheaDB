import csv

class Table_API:
    def __init__(self, db) -> None:
        self.db = db
 
    # Inserting
    def insertValue(self, tbName, value):
        str = f"INSERT INTO ({tbName}) VALUES ({value});"
        print(str)
def insertOne(file_path):
    tableName = "''"
    tableValue = ''
    count = 0

    ta=Table_API("r")
    with open(file_path, "r") as f:
        for line in f.readlines():
            if count==0:
                tableName=line.rstrip()
                count+=1
            else:
                ta.insertValue(tableName,line.rstrip())
    
if __name__ == "__main__":
    insertOne("Credentials.txt")
    insertOne("Customer.txt")
    insertOne("MenuItem.txt")
    insertOne("OrderRequest.txt")
    insertOne("Receipt.txt")
    insertOne("Resource.txt")
    insertOne("Schedule.txt")
    insertOne("Service.txt")
    insertOne("Supplier.txt")
