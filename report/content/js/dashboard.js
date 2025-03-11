/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if (headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if (info.overall) {
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for (var index = 0; index < data.length; index++) {
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]) : data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if (seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for (var index = 0; index < info.items.length; index++) {
        var item = info.items[index];
        if ((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
            &&
            (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)) {
            if (item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for (var col = 0; col < item.data.length; col++) {
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList: defaultSorts});
}

$(document).ready(function () {

    // Customize table sorter default options
    $.extend($.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 100.0, "KoPercent": 0.0};
    var dataset = [
        {
            "label": "FAIL",
            "data": data.KoPercent,
            "color": "#FF6347"
        },
        {
            "label": "PASS",
            "data": data.OkPercent,
            "color": "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series: {
            pie: {
                show: true,
                radius: 1,
                label: {
                    show: true,
                    radius: 3 / 4,
                    formatter: function (label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background: {
                        opacity: 0.5,
                        color: '#000'
                    }
                }
            }
        },
        legend: {
            show: true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {
        "supportsControllersDiscrimination": true,
        "overall": {"data": [0.04335, 500, 1500, "Total"], "isController": false},
        "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"],
        "items": [{
            "data": [1.0E-4, 500, 1500, "HTTP Request_query"],
            "isController": false
        }, {
            "data": [0.0731, 500, 1500, "HTTP Request_update"],
            "isController": false
        }, {"data": [0.05685, 500, 1500, "HTTP Request_create"], "isController": false}]
    }, function (index, item) {
        switch (index) {
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {
        "supportsControllersDiscrimination": true,
        "overall": {
            "data": ["Total", 30000, 0, 0.0, 28929.57623333336, 0, 99459, 32802.5, 65181.9, 77343.9, 93571.81000000003, 58.47474475773913, 296.6614834287446, 14.825984108635355],
            "isController": false
        },
        "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"],
        "items": [{
            "data": ["HTTP Request_query", 10000, 0, 0.0, 41378.86150000011, 327, 99459, 40450.0, 71439.79999999999, 81517.09999999996, 96542.67, 19.494887465762105, 282.28762109675796, 3.615130789372362],
            "isController": false
        }, {
            "data": ["HTTP Request_update", 10000, 0, 0.0, 18112.953600000037, 0, 83675, 11671.5, 43968.8, 53517.649999999994, 77142.21999999999, 21.058439274831585, 6.988905262556621, 5.429128875542518],
            "isController": false
        }, {
            "data": ["HTTP Request_create", 10000, 0, 0.0, 27296.913599999967, 0, 83697, 26166.5, 54285.8, 65029.799999999996, 80041.7, 22.149326328239727, 9.03723445380315, 7.029815485037022],
            "isController": false
        }]
    }, function (index, item) {
        switch (index) {
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
                // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {
        "supportsControllersDiscrimination": false,
        "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"],
        "items": []
    }, function (index, item) {
        switch (index) {
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

    // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {
        "supportsControllersDiscrimination": false,
        "overall": {"data": ["Total", 30000, 0, "", "", "", "", "", "", "", "", "", ""], "isController": false},
        "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"],
        "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {
            "data": [],
            "isController": false
        }]
    }, function (index, item) {
        return item;
    }, [[0, 0]], 0);

});
