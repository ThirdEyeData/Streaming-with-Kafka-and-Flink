/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spendreport;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.CsvTableSource;


public class SpendReport {
    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


        BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);
        //Creating a Table Source to read from csv file and specifying its field name and datatypes
        CsvTableSource transactionSource = CsvTableSource.builder()
                .path("/demo/bank_data.csv")
                .ignoreFirstLine()
                .field("account_no", Types.STRING)
                .field("date", Types.SQL_TIMESTAMP)
                .field("transaction_details", Types.STRING)
                .field("value_date", Types.STRING)
                .field("transaction_type", Types.STRING)
                .field("amount", Types.BIG_DEC)
                .field("balance", Types.DOUBLE)
                .build();

        // Creating Table Sink where the resultant output will be dumped into
        TableSink csvSink = new CsvTableSink("/demo/out.csv", ",", 1, FileSystem.WriteMode.OVERWRITE);
        String[] fieldNames = {"account_no", "start_time", "end_time", "amount"};
        TypeInformation[] fieldTypes = {Types.STRING, Types.SQL_TIMESTAMP, Types.SQL_TIMESTAMP, Types.BIG_DEC};

        //Registering table source and sink
        tEnv.registerTableSource("transactions", transactionSource);
        tEnv.registerTableSink("transactionsSink", fieldNames, fieldTypes, csvSink);

        /*Performing aggregation of data using window of 365 days(1 year) .
        selecting account_no, window start time, window end time and sum of amount .
        Dumping the selected data into registered table sink. */

        tEnv
                .scan("transactions")
                .window(Tumble.over("365.day").on("date").as("w"))
                .groupBy("w,account_no")
                .select("account_no,w.start,w.end,amount.sum as total")
                .where("total > 1000000000")
                .insertInto("transactionsSink");


        env.execute("transactions");
    }
}
