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

	public static void main(String[] args) throws Exception {

		////		//create statement like the structure we are going to have
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

		// set to true to populate the LRS with test data
		boolean popTestData = false;
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
			
//			// object 
//			AgentActivity otherTarget = new AgentActivity();
//			otherTarget.setObjectType("Agent");
//			otherTarget.setMbox(doctor.getMbox());
//			otherTarget.setName(doctor.getName());
//
//			Statement OtherStatement = new Statement();
//			OtherStatement.setActor(angelBob);
//			OtherStatement.setVerb(verb);
//			OtherStatement.setObject(otherTarget);
//			OtherStatement.stamp();
//			
//			System.out.print(OtherStatement.getObject());
//			
//			getDoctors.PostTestStatements(howMany, OtherStatement);
		}

//		// Gets the statements
//		ArrayList<Statement> onlyBobs = getBob.GetStatements();
//		System.out.println("from LRS, bob as object " + onlyBobs.size());
		// Gets the statements
		for (int i = 0; i < 1; i++) {
			DateTime start = new DateTime().now();
			ArrayList<Statement> onlyDocs = getMaster.GetStatements();
			DateTime end = new DateTime().now();
			Interval interval = new Interval(start, end);
			System.out.println("duration of call " + interval.toDurationMillis());
			System.out.println("from LRS, master as object " + onlyDocs.size());
		}	
		}
		

}
