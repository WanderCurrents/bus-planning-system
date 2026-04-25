//  XMLHandler Class
//----------------------
//Description: superclass that deals with XML handling with the XML related libraries in Java. Generic enough for all XML uses in this program and is to be extended
//Attributes:	doc : Document
//				filePath : String
//Methods:	XMLHandler(path : String)
//			removeWhitespaceNodes(node : Node) : void
//			saveXML() : void

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
	
	protected Document doc;		//In-memory version of XML file that Java uses
	protected String filePath;	//The file path that points to the XML file
	
	//Basic constructor for the superclass
	public XMLHandler(String path) throws Exception
	{
		filePath = path;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(new File(filePath));
	}
	
	//A bug was found early-on where whitespace would be added everywhere anytime an operation was done. This method was developed to prevent this
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
	
	//Saves document to XML file
	protected void saveXML() throws Exception
	{
		removeWhitespaceNodes(doc.getDocumentElement());
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");   //Indent fix
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  //Indent amount for fix
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
	}
}

//"You only get to die for having lived. Most people who could ever exist, will never even be born." -Neil deGrasse Tyson