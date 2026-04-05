//Main class for XML file handling
//---------------------------------------------------------------
//Fields
//	idIndex = the next available ID value, used for adding a new element
//	doc = necessary in-memory version of the XML file that Java uses
//	filePath = String field that has the file path for the XML file
//Methods
//	getIDIndex(list) = moreso establishes the next working ID index, not really a getter
//	removeWhidespaceNodes(node) = fixes indent issue found in early testing
//	saveXML() = saves the document to the XML file

package xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;

public class XMLHandler 
{
	
	protected Document doc;
	protected String filePath;
	
	public XMLHandler(String path) throws Exception
	{
		filePath = path;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(new File(filePath));
		
	}
	

	protected void removeWhitespaceNodes(Node node)
	{
		NodeList children = node.getChildNodes();  //Gets all children nodes
		for(int i = children.getLength() - 1; i >= 0; i--)  //Loops backward through children to avoid skipping problems
		{
			Node child = children.item(i);  //Gets current child node
			//tests if a text node and trims it to get only text
			if(child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty())
			{
				node.removeChild(child);   //if only white space, remove it
			}
			else if(child.hasChildNodes())  //Recursive call to traverse tree to get everything
			{
				removeWhitespaceNodes(child);
			}
			
		}
	}
	
	protected void saveXML() throws Exception
	{
		removeWhitespaceNodes(doc.getDocumentElement());
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");   //Indent fix
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  //Indent amount for fix //TODO: Check if this is necessary or if we can manually add it
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));   //TODO: make sure the filePath solution actually works
		transformer.transform(source, result);
	}
	
	
	
}
