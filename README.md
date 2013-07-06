CCD_Decoding-To-HumanReadableForm:
==================================
CCD stands for Continuity of Care Document <br>
This repository contains program that decodes any given CCD document to HRF(Human Readable form), 
In this assignment, HRF is referring to Text File which is outputted by the program.

Note: The program in this repository doesn't decode all sections of CCD document.

About CCD:
=========
The Continuity of Care Document (CCD) specification is an XML-based markup standard intended to 
specify the encoding, structure, and semantics of a patient summary clinical document for exchange.<br>
<b>Structure:</b><br>
The CCD specification is a constraint on the HL7 Clinical Document Architecture (CDA) standard. The CDA 
specifies that the content of the document consists of a mandatory textual part (which ensures human 
interpretation of the document contents) and optional structured parts (for software processing). The 
structured part is based on the HL7 Reference Information Model (RIM) and provides a framework for 
referring to concepts from coding systems, such as the SNOMED or the LOINC.
The patient summary contains a core data set of the most relevant administrative, demographic, and 
clinical information facts about a patient's healthcare, covering one or more healthcare encounters. It 
provides a means for one healthcare practitioner, system, or setting to aggregate all of the pertinent data 
about a patient and forward it to another practitioner, system, or setting to support the continuity of care. 
Its primary use case is to provide a snapshot in time containing the pertinent clinical, demographic, and 
administrative data for a specific patient.

Visit link for more info. on CCD: http://en.wikipedia.org/wiki/Continuity_of_Care_Document

Source Code File:
================= 
CCD_Decode.java      &nbsp;&nbsp;&nbsp;   in 'src' folder

Examples of Input & Output File:
================================
Output-SampleCCDDocument-QSG-level-3.txt --- Output File
Input-SampleCCDDocument-QSG-level-3.xml --- Input File (CCD Format)

For more Info.:
===============
Read comments in CCD_Decode.java
