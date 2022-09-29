import csv, json

class Journal:
    def __init__(self, sourceId, name, issn, quartile, h_index, country, publisher, coverage, categories):
        self.name = name
        self.metrics = []
        if quartile != '-':
            self.metrics.append({"bibliometric": "Quartile", "score": quartile, "year": coverage[-4:], "source": "scimagojr.com"})
        self.metrics.append({"bibliometric": "H-index", "score": h_index, "year": coverage[-4:], "source": "scimagojr.com"})
        self.scimagoId = sourceId
        self.issn = issn
        self.country = country
        self.publisher = publisher
        self.categories = categories.replace('; ', '\n')

with open('scimagojr_input.csv', mode='r') as csv_file:
    csv_reader = csv.DictReader(csv_file, delimiter=';')
    line_count = 0
    journals = []
    for row in csv_reader:
        if line_count == 0:
            print(f'Column names are {", ".join(row)}')
            line_count += 1
        else:
            journals.append(Journal(row['Sourceid'], row['Title'], row['Issn'], row['SJR Best Quartile'], row['H index'], row['Country'], row['Publisher'], row['Coverage'], row['Categories']))
            line_count += 1
    print(f'Processed {line_count} lines.')
    jsonStr = json.dumps([journal.__dict__ for journal in journals])
    f = open("scimago_output.json", "w")
    f.write(jsonStr)
    f.close()