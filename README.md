# FxMatcherEngine

load this in to IntelliJ Idea platform.
Run this application as it is without provding any input and output files, input and output files are already specified if they are not given explicitly
input csv file location : src/main/resources/exampleOrders.csv
output csv file : outputOrders.csv(will be generated in main project file once application is executed)


to run this application, generated a jar and use below command in cmd
modify below command with exact file paths and names
java -cp jar_filename.jar com.matcher.OrderMatcher input_csv_file.csv output_csv_file.csv
