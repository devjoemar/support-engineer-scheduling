
### Support Engineer Scheduling Application

##### Overview
This application automates the scheduling assignment for available support engineers, enabling fair distribution of support shifts among a team of engineers.

##### Scheduling Rules
The application adheres to the following rules to ensure efficient and fair scheduling:
1. **No Consecutive Shifts:** An engineer cannot have support shifts on consecutive days.
2. **Maximum Shifts:** Each engineer should complete a specific number of shifts in a given period, ensuring a fair distribution of workload.
3. **Weekend Exclusion:** The schedule excludes weekends, with shifts assigned only on weekdays.

##### Features
- **Automated Scheduling:** Generates schedules for a specified number of engineers over a set period.
- **Flexible Date Ranges:** Supports custom date ranges and specific numbers of days.
- **Fair Distribution:** Strictly adheres to scheduling rules to ensure fairness and efficiency.

###### Sample Input and Response

###### 1. Fixed Number of Days from Current Date
**Endpoint:** `POST http://localhost:8080/api/v1/schedule`

**Payload:**
```json
{
    "totalEngineers": 5,
    "numberOfDays": 5
}
```
*Description:* Generates schedules for the next consecutive days based on the current date. Assumes today is January 4, 2024.

**Sample Response:**
```json
{
    "status": "OK",
    "httpStatus": 200,
    "message": "created",
    "internalCode": "SCHEDULE-7",
    "data": {
        "schedules": [
            {
                "date": "2024-01-05",
                "engineers": [
                    {
                        "id": 3,
                        "name": "Engineer 3"
                    },
                    {
                        "id": 1,
                        "name": "Engineer 1"
                    }
                ]
            },
            {
                "date": "2024-01-08",
                "engineers": [
                    {
                        "id": 0,
                        "name": "Engineer 0"
                    },
                    {
                        "id": 2,
                        "name": "Engineer 2"
                    }
                ]
            },
            {
                "date": "2024-01-09",
                "engineers": [
                    {
                        "id": 3,
                        "name": "Engineer 3"
                    },
                    {
                        "id": 4,
                        "name": "Engineer 4"
                    }
                ]
            }
        ]
    }
}
```

###### 2. Fixed Number of Days from a Specific Start Date
**Endpoint:** `POST http://localhost:8080/api/v1/schedule`

**Payload:**
```json
{
   "totalEngineers": 4,
   "numberOfDays": 5,
   "startDate": "2022-01-01"
}
```
*Description:* Generates schedules based on the specified "startDate" and includes the next consecutive dates.

**Sample Response:**
```json
{
    "status": "OK",
    "httpStatus": 200,
    "message": "created",
    "internalCode": "SCHEDULE-7",
    "data": {
        "schedules": [
            {
                "date": "2022-01-03",
                "engineers": [
                    {
                        "id": 0,
                        "name": "Engineer 0"
                    },
                    {
                        "id": 3,
                        "name": "Engineer 3"
                    }
                ]
            },
            {
                "date": "2022-01-04",
                "engineers": [
                    {
                        "id": 2,
                        "name": "Engineer 2"
                    },
                    {
                        "id": 1,
                        "name": "Engineer 1"
                    }
                ]
            },
            {
                "date": "2022-01-05",
                "engineers": [
                    {
                        "id": 0,
                        "name": "Engineer 0"
                    },
                    {
                        "id": 3,
                        "name": "Engineer 3"
                    }
                ]
            }
        ]
    }
}
```

###### 3. Specific Date Range
**Endpoint:** `POST http://localhost:8080/api/v1/schedule`

**Payload:**
```json
{
   "totalEngineers": 10,
   "startDate": "2024-01-08",
   "endDate": "2024-01-14"
}
```
*Description:* Generates schedules for a specific date range.

**Sample Response:**
```json
{
    "status": "OK",
    "httpStatus": 200,
    "message": "created",
    "internalCode": "SCHEDULE-7",
    "data": {
        "schedules": [
            {
                "date": "2024-01-08",
                "engineers": [
                    {
                        "id": 8,
                        "name": "Engineer 8"
                    },
                    {
                        "id": 1,
                        "name": "Engineer 1"
                    }
                ]
            },
            {
                "date": "2024-01-09",
                "engineers": [
                    {
                        "id": 9,
                        "name": "Engineer 9"
                    },
                    {
                        "id": 7,
                        "name": "Engineer 7"
                    }
                ]
            },
            {
                "date": "2024-01-10",
                "engineers": [
                    {
                        "id": 5,
                        "name": "Engineer 5"
                    },
                    {
                        "id": 3,
                        "name": "Engineer 3"
                    }
                ]
            },
            {
                "date": "2024-01-11",
                "engineers": [
                    {
                        "id": 0,
                        "name": "Engineer 0"
                    },
                    {
                        "id": 1,
                        "name": "Engineer 1"
                    }
                ]
            },
            {
                "date": "2024-01-12",
                "engineers": [
                    {
                        "id": 8,
                        "name": "Engineer 8"
                    },
                    {
                        "id": 5,
                        "name": "Engineer 5"
                    }
                ]
            }
        ]
    }
}
```
