package com.example.timecard.Models.Constants;

public enum DatabaseFiles
{
	MAIN_DB_FILE_PATH{
		@Override
		public String toString()
		{
			return "src/main/java/com/example/timecard/Models/Database/";
		}
	},

	EMPLOYEE_DATABASE{
		@Override
		public String toString()
		{
			return "emp_database.sqlite";
		}
	},

	EMPS_TABLE

}
