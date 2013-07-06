/* CCD_Decode
 * Description:
 *   1) This project decodes Electronic Medical Records(EMRs) which are in CCD form to
 *      Text format(or Human Readable form) 
 *   2) This program takes Input file Name as argument and that file should be outside the 
 *      src folder, otherwise Enter full Path of Input file  
 *   3) It reads from an Input CCD file and writes to another text file
 *      with same name having CCD file, but with .text extension.
 */
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import org.openhealthtools.mdht.uml.cda.Act;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.DocumentationOf;
import org.openhealthtools.mdht.uml.cda.Entry;
import org.openhealthtools.mdht.uml.cda.EntryRelationship;
import org.openhealthtools.mdht.uml.cda.Participant2;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Performer1;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.cda.ccd.CCDPackage;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.util.ValidationResult;
import org.openhealthtools.mdht.uml.hl7.datatypes.ANY;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.SXCM_TS;

public class CCD_Decode {
	
	public static void main(String[] args) throws Exception{
	   
		if(args.length == 1){
		  if(args[0].contains(".xml")) {
			  String fileName = args[0];
			  
			  FileWriter fstream1 = new FileWriter(fileName.replace(".xml", ".text"));
				BufferedWriter out = new BufferedWriter(fstream1);
				CCDPackage.eINSTANCE.eClass();	           // static package registration
				
				// validate on load, also create validation result to hold diagnostics
				ValidationResult result = new ValidationResult();
				FileInputStream f_in = new FileInputStream(fileName);
		
				ClinicalDocument ccdDocumentIn=(ClinicalDocument) CDAUtil.load(f_in,result );
				
				out.write("Patient Profile");
		        out.newLine();
		        out.write("===============\n");
		        out.newLine();
		        String tab2 = "            ";
		        String tab = "      ";
		        
		        // Decoding Patient Role Section
		        for (PatientRole patient_role: ccdDocumentIn.getPatientRoles() ) {
		        	
		        	out.write(tab+"Patient");
		        	out.newLine();
		        	out.write(tab+"-------");
		        	out.newLine();
		        	// Patient First and Last Name
		        	String lastName = patient_role.getPatient().getNames().get(0).getFamilies().get(0).getText();
		            String firstName = patient_role.getPatient().getNames().get(0).getGivens().get(0).getText();
		            out.write(tab2+"First name:" +tab + firstName);
		            out.newLine();
		            out.write(tab2+"Last name: " + tab + lastName);
		            out.newLine();
		            //Patient Gender
		            if(patient_role.getPatient().getAdministrativeGenderCode() != null) {
		            	out.write(tab2+"Gender:"+tab+patient_role.getPatient().getAdministrativeGenderCode().getCode());
		            	out.newLine();
		            }else {
		            	out.write(tab2+"Gender:"+tab+"NA");
		            	out.newLine();
		            	}
		            //Marital Status
		            if(patient_role.getPatient().getMaritalStatusCode() != null) {
		            	out.write(tab2+"Marital Status:"+tab+patient_role.getPatient().getMaritalStatusCode().getCode());
		            	out.newLine();
		            }else {
		            	out.write(tab2+"Marital Status:"+tab+"NA");
		            	out.newLine();
		            }
		            // Ethnicity
		            if(patient_role.getPatient().getEthnicGroupCode()!=null){
		            	out.write(tab2+"Ethnicity:"+tab+patient_role.getPatient().getEthnicGroupCode().getDisplayName());
		            	out.newLine();
		            }else {
		            	out.write(tab2+"Ethnicity:"+tab+"NA");
		            	out.newLine();
		            }
		           
		            //Language
		            if((patient_role.getPatient().getLanguageCommunications().size()!=0) &&
		            		(patient_role.getPatient().getLanguageCommunications().get(0).getLanguageCode().getDisplayName()!=null)){
		            	
		            	out.write(tab2+"Language:"+tab+patient_role.getPatient().getLanguageCommunications().get(0).getLanguageCode().getDisplayName());//.getLanguageCode().getCode());
		            	out.newLine();
		            	
		        	}else {
		        		out.write(tab2+"Language:"+tab+"NA");
		        		out.newLine();
		        	}
		            //Address

					if (!patient_role.getAddrs().isEmpty()){
						String street = patient_role.getAddrs().get(0).getStreetAddressLines().get(0).getText();
						String city = patient_role.getAddrs().get(0).getCities().get(0).getText();
						String state = patient_role.getAddrs().get(0).getStates().get(0).getText();
						String zipcode = patient_role.getAddrs().get(0).getPostalCodes().get(0).getText();
						out.write(tab2+"Address: "+tab+ street + ", " + city + ", " + state + " " + zipcode);
						out.newLine();
					}else {
						out.write(tab2+"Address:     NA" );
						out.newLine();
					}
					
					//DOB
					// birthTime value is in yyyymmdd format
					if(!patient_role.getPatient().getBirthTime().getValue().isEmpty()){ 
		                String birthDate = ""+patient_role.getPatient().getBirthTime().getValue();
		                out.write(tab2+"Date of Birth: "+tab+ birthDate.substring(6, 8)+"/"+birthDate.substring(4, 6) +"/"+birthDate.substring(0, 4));
		                out.newLine();
					}    
		            else { 
		            	out.write(tab2+"Date of Birth: "+tab +"NA");
		            	out.newLine();
		            }
					
		        }//end of patient role
		        
		        out.newLine();
		        out.write(tab+"Care Provider\n");
		        out.write(tab+"-------------\n");
		        
		    	//<documentationOf> has info about care providers including pharmacies
		    	for (DocumentationOf df:ccdDocumentIn.getDocumentationOfs()){
		    		for (Performer1 p:df.getServiceEvent().getPerformers()){
		    			if (p.getFunctionCode().getCode().equals("PCP")||p.getFunctionCode().getCode().equals("PP")){//primary care provider, usually PP
		    				String givenName=""+p.getAssignedEntity().getAssignedPerson().getNames().get(0).getGivens().get(0).getText();
		    				String lastName=""+p.getAssignedEntity().getAssignedPerson().getNames().get(0).getFamilies().get(0).getText();
		    				out.write(tab2+"Primary Care:"+tab + lastName + ", " + givenName);
		    				out.newLine();
		    				if (!p.getAssignedEntity().getTelecoms().isEmpty())
		    					out.write(tab2+"             "+tab + p.getAssignedEntity().getTelecoms().get(0).getValue()+"\n");
		    			}
		    		}
		    		
		    	}
		    	
		    	
		        for (Section section: ccdDocumentIn.getSections()) {
						if (section.getCode()!=null){
							if (section.getCode().getCode().equals("48765-2")){  //codeSystemName="LOINC" 
								out.newLine();
								out.write(tab+"Allergies: \n");
								out.write(tab+"-----------\n");
								int i=1;
								for (Act act: section.getActs() ) {
									for (EntryRelationship er: act.getEntryRelationships() ) {
										for (ANY val: er.getObservation().getValues() ) 
											out.write(tab2+i+". "+ ((CD) val).getDisplayName()+"\n" );
										
										for (Participant2 participant: er.getObservation().getParticipants() ) 
											out.write(tab2+ tab+participant.getParticipantRole().getPlayingEntity().getCode().getDisplayName() ); 
									
										for (EntryRelationship er_val: er.getObservation().getEntryRelationships() )
											if (!((CD) er_val.getObservation().getValues().get(0)).getDisplayName().isEmpty())
												out.write(tab+ ((CD) er_val.getObservation().getValues().get(0)).getDisplayName() );
										out.newLine();
										i++;
									}
								
								}
							}
							if (section.getCode().getCode().equals("11450-4")){  //codeSystemName="LOINC" 
								out.newLine();
								out.write(tab+"Health Problems: \n");
								out.write(tab+"-----------------\n");
								String date="";

								int j=1;
								
								for (Act act: section.getActs() ) {
									for (EntryRelationship er: act.getEntryRelationships() ) {
										if (er.getObservation().getEffectiveTime()!=null){
											date=er.getObservation().getEffectiveTime().getLow().getValue();
											date=date.substring(0, 4);
											}
			
										for (ANY val: er.getObservation().getValues() ) 
											out.write(tab2+j+". "+date + tab+((CD) val).getDisplayName()+tab);
										
										for (Participant2 participant: er.getObservation().getParticipants() ) 
											out.write(tab+ participant.getParticipantRole().getPlayingEntity().getCode().getDisplayName()+"\n" ); 
									j++;
										
									}
								
								}
								out.newLine();
							}
							
						}
						if (section.getTitle().getText().equalsIgnoreCase("Immunizations")){
							int i=1;
							out.newLine();
							out.write(tab+"Immunizations: \n");
							out.write(tab+"---------------\n");
							for (Entry entry  : section.getEntries() ) {
								String date="";
								for  (SXCM_TS t: entry.getSubstanceAdministration().getEffectiveTimes()){
									if (t.getValue()!=null)
									date=date+t.getValue().substring(4, 6)+"/"+t.getValue().substring(0, 4);				

								}
								out.write(tab2+i+". "+date+tab+
										entry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().getDisplayName()
										+"\n");
								i++;
							}
							out.newLine();
						}
						int i=1;
						if (section.getTitle().getText().equalsIgnoreCase("Medications")){
							out.newLine();
							out.write(tab+"Medications: \n");
							out.write(tab+"-------------\n");
							for (Entry entry  : section.getEntries() ) {
								 String medication=entry.getSubstanceAdministration().getConsumable().getManufacturedProduct().getManufacturedMaterial().getCode().getDisplayName();
								 out.write(tab2+i+". "+medication+"\n");
								
								 String precondition="NA";
								 //this section not working properly for some templates who do not have precondition part
								if (!entry.getSubstanceAdministration().getPreconditions().isEmpty())
								 precondition= ((CD) entry.getSubstanceAdministration().getPreconditions().get(0).getCriterion().getValue()).getDisplayName();
								 out.write(tab2+"   Precondition: "+precondition);
								 
								 
								 i++;
							}
							}
						out.newLine();
						}
		        out.close();
		  }else
			  System.out.println("File should be an XML file");
	   }
	   else { 
		   System.out.println("Syntax error: Accepts only one argument: <fileName In XML>");
	   }
	
	} // Main Function...  
}  // Class CCD_Decode ends here...
