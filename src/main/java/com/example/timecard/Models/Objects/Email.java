package com.example.timecard.Models.Objects;

import java.io.File;

public class Email
{
	private String to;
	private String subject;
	private String content;
	private File attachment;

	public Email(String to, String subject, String content)
	{
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public void addAttachment(File attachment)
	{
		this.attachment = attachment;
	}

	public String getTo()
	{
		return to;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getContent()
	{
		return content;
	}
}
