import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.AgentActivity;
import com.rusticisoftware.tincan.Context;
import com.rusticisoftware.tincan.LanguageMap;
import com.rusticisoftware.tincan.RemoteLRS;
import com.rusticisoftware.tincan.Result;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.StatementRetreiver;
import com.rusticisoftware.tincan.StatementsResult;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;
import com.rusticisoftware.tincan.v10x.StatementsQuery;



public class Sp2Wrapper {

	/*
	 * This shows the simplest examples of the class StatementRetreiver
	 * */
	public static void SimpleExamples() throws MalformedURLException, Exception
	{
		// the LRS creds
		String endpoint = "https://sp2.waxlrs.com/TCAPI/";
		String username = "NIRzUtWx0rSpEsa0IXt9";
		String password = "lJLm0WQTwQwJQ8tZzeQV";
		
		//sample emails
		String targetEmailExample1 = "mailto:jim.beam@us.army.mil";
		String targetEmailExample2 = "pilot@sp2.com";		
		
		// with out setting the LRS creds
		StatementRetreiver getStatements1= new StatementRetreiver()
		.TargetEmail(targetEmailExample1);

		ArrayList<Statement> statements = getStatements1.GetStatements();
		System.out.println("from LRS, observer as object " + statements.size());
		
		// TO access different fields, the classes have built in functions, below is an exmaple of how to get a few select fields
		for (Statement statement : statements) {
			//Gets response from object
			//Gets response from object
			String response = (statement.getResult() == null) ? null : statement.getResult().getResponse();
			String KSAName = "";
			String eventName = "";
			String eventDate = "";
			if (statement.getContext() != null)
			{
				if (statement.getContext().getContextActivities() != null)
				{
					if (statement.getContext().getContextActivities().getGrouping() != null)
					{
						if (statement.getContext().getContextActivities().getGrouping().get(0) != null)
						{	
							if (statement.getContext().getContextActivities().getGrouping().get(0).getDefinition() != null)
							{
								if ( statement.getContext().getContextActivities().getGrouping().size() > 1)
								{
									KSAName = statement.getContext().getContextActivities().getGrouping().get(0).getDefinition().getName().get("en-us");
									eventName = statement.getContext().getContextActivities().getGrouping().get(1).getDefinition().getName().get("en-us");
									eventDate = statement.getContext().getContextActivities().getGrouping().get(1).getDefinition().getDescription().get("en-us");
								}
								else
								{
									KSAName = statement.getContext().getContextActivities().getGrouping().get(0).getDefinition().getName().get("en-us");
								}
							}
						}
					}
				}
			}			
			
			System.out.println(String.format("%s , %s,  %s, %s", response, KSAName, eventName, eventDate));
		}		
	}
	
	
	/*
	 * This calls the simple example and also has options to populate the LRS with test data and to "load test" this part of the system
	 * */
	public static void main(String[] args) throws Exception {

		// runs  simple examples
		SimpleExamples();
		
		// set to true to populate the LRS with test data
		boolean popTestData = false;
		
		// Gets the statements, loads tests
		boolean loadTest = false;
		
		//create statement like the structure we are going to have
		// actor
		Agent doctor = new Agent();
		doctor.setMbox("mailto:observer@sp2.com");
		doctor.setName("The Doctor");

		//agent object
		String angelBobEmail = "pilot@sp2.com";
		Agent angelBob = new Agent();
		angelBob.setMbox("mailto:" + angelBobEmail);
		angelBob.setName("Angel Bob");

		//agent object
		Agent theMaster = new Agent();
		theMaster.setMbox("mailto:master@everywhere.com");
		theMaster.setName("The Master");


		// the LRS creds
		String endpoint = "https://sp2.waxlrs.com/TCAPI/";
		String username = "NIRzUtWx0rSpEsa0IXt9";
		String password = "lJLm0WQTwQwJQ8tZzeQV";

		// with out setting the LRS creds
		StatementRetreiver getMaster = new StatementRetreiver()
		.TargetEmail(theMaster.getMbox());

		// with out setting the LRS creds
		StatementRetreiver getDoctor= new StatementRetreiver()
		.TargetEmail(doctor.getMbox());

		// with setting the LRS creds
		StatementRetreiver getBob = new StatementRetreiver()
		.TargetEmail(angelBobEmail)
		.Endpoint(endpoint)
		.Username(username)
		.Password(password);

		if (loadTest)
		{
			for (int i = 0; i < 1; i++) {
				DateTime start = new DateTime().now();
				ArrayList<Statement> onlyDocs = getMaster.GetStatements();
				DateTime end = new DateTime().now();
				Interval interval = new Interval(start, end);
				System.out.println("duration of call " + interval.toDurationMillis());
				System.out.println("from LRS, master as object " + onlyDocs.size());
			}
		}
		else
		{
			ArrayList<Statement> onlyDocs = getBob.GetStatements();
			System.out.println("from LRS, bob as object " + onlyDocs.size());
		}
	}


}
