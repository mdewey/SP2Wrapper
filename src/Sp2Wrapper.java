import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.AgentActivity;
import com.rusticisoftware.tincan.LanguageMap;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.StatementRetreiver;
import com.rusticisoftware.tincan.Verb;



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
		String targetEmailExample1 = "mailto:observer@sp2.com";
		String targetEmailExample2 = "pilot@sp2.com";		
		
		// with out setting the LRS creds
		StatementRetreiver getDoctor= new StatementRetreiver()
		.TargetEmail(targetEmailExample1);

		ArrayList<Statement> statements = getDoctor.GetStatements();
		System.out.println("from LRS, bob as object " + statements.size());
		
		// with setting the LRS creds
		StatementRetreiver getBob = new StatementRetreiver()
		.TargetEmail(targetEmailExample2)
		.Endpoint(endpoint)
		.Username(username)
		.Password(password);
		
		ArrayList<Statement> statements2 = getBob.GetStatements();
		System.out.println("from LRS, bob as object " + statements2.size());
		
		// one line -- define and get statements
		ArrayList<Statement> statements3 = new StatementRetreiver()
		.TargetEmail(targetEmailExample2)
		.Endpoint(endpoint)
		.Username(username)
		.Password(password)
		.GetStatements();
		
		System.out.println("from LRS, bob as object " + statements2.size());
		
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


		int howMany = 1; 
		if (popTestData)
		{	
			//		// verb
			Verb verb = new Verb();
			LanguageMap display = new LanguageMap();
			display.put("en", "assessed");
			verb.setDisplay(display);
			verb.setId("http://www.example.com/VerbId");
			//
			// object 
			AgentActivity target = new AgentActivity();
			target.setObjectType("Agent");
			target.setMbox(theMaster.getMbox());
			target.setName(theMaster.getName());
			//
			Statement st = new Statement();
			st.stamp(); // triggers a PUT -- sure?

			st.setActor(doctor);
			st.setVerb(verb);
			st.setObject(target);


			getMaster.PostTestStatements(howMany, st);

			// object 
			AgentActivity otherTarget = new AgentActivity();
			otherTarget.setObjectType("Agent");
			otherTarget.setMbox(doctor.getMbox());
			otherTarget.setName(doctor.getName());

			Statement OtherStatement = new Statement();
			OtherStatement.setActor(angelBob);
			OtherStatement.setVerb(verb);
			OtherStatement.setObject(otherTarget);
			OtherStatement.stamp();

			System.out.print(OtherStatement.getObject());

			getDoctor.PostTestStatements(howMany, OtherStatement);
		}

	
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
