package com.project.timecard.Models.Constants;

public enum DatabaseFiles
{
	MAIN_DB_FILE_PATH{
		@Override
		public String toString()
		{
			return "src/main/java/com/project/timecard/Models/Database/";
		}
	},

	EMPLOYEE_DATABASE_FILE_NAME {
		@Override
		public String toString()
		{
			return "emp_database.sqlite";
		}
	},

	EMPS_TABLE

}
