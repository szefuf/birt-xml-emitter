package com.digiassn.blogsport.birt.emitter.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.xerces.impl.dv.util.Base64;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;


/**
 * As a BIRT integration and extension developer
 * I need an XML emitter for BIRT
 * So I can easily Unit Test run results using XPath
 * 
 * The purpose of this emitter is to allow a structured output of BIRT reports that can
 * easily be queried using XPath expressions or whatever. This is strictly for report output, this
 * does not take into account formatting.
 * 
 * @author John Ward
 *
 */
public class XMLEmitter implements IContentEmitter {

	private StringBuilder stringBuilder;
	private OutputStream outputStream;

	/**
	 * Utility method that will append based on a content element
	 * @param content
	 */
	private void appendAttribute(IContent content)
	{
		if (content.getBookmark() != null)
		{
			appendAttribute("id", content.getBookmark());
		}
		
		if (content.getName() != null)
		{
			appendAttribute("name", content.getName());
		}
	}
	
	/**
	 * A utility method that will append a XML attribute
	 * to the stringbuilder
	 * @param key
	 * @param value
	 */
	private void appendAttribute(String key, String value)
	{
		stringBuilder.append(" ");
		stringBuilder.append(key);
		stringBuilder.append("=\"");
		stringBuilder.append(value);
		stringBuilder.append("\"");
	}
	
	@Override
	/**
	 * Given a initialized output stream
	 * and a populated stringbuilder
	 * when end is called
	 * then stringbuilder should be written to the outputstream.
	 */
	public void end(IReportContent reportContent) throws BirtException {
		stringBuilder.append("</report>");
		try {
			outputStream.write(stringBuilder.toString().getBytes());
			outputStream.close();
		}
		catch (IOException e) {
			throw new BirtException(e.getMessage());
		}
	}

	@Override
	public void endCell(ICellContent arg0) throws BirtException {
		stringBuilder.append("</cell>");
	}

	@Override
	public void endContainer(IContainerContent arg0) throws BirtException {
		stringBuilder.append("</container>");
	}

	@Override
	public void endContent(IContent arg0) throws BirtException {
		stringBuilder.append("</content>");
	}

	@Override
	public void endGroup(IGroupContent arg0) throws BirtException {
		stringBuilder.append("</group>");
	}

	@Override
	public void endList(IListContent arg0) throws BirtException {
		stringBuilder.append("</list>");
	}

	@Override
	public void endListBand(IListBandContent arg0) throws BirtException {
		stringBuilder.append("</list-band>");
	}

	@Override
	public void endListGroup(IListGroupContent arg0) throws BirtException {
		stringBuilder.append("</list-group>");
	}

	@Override
	public void endPage(IPageContent arg0) throws BirtException {
		stringBuilder.append("</page>");
	}

	@Override
	public void endRow(IRowContent arg0) throws BirtException {
		stringBuilder.append("</row>");
	}

	@Override
	public void endTable(ITableContent arg0) throws BirtException {
		stringBuilder.append("</table>");
	}

	@Override
	public void endTableBand(ITableBandContent arg0) throws BirtException {
		stringBuilder.append("</table-band>");
	}

	@Override
	public void endTableGroup(ITableGroupContent arg0) throws BirtException {
		stringBuilder.append("</table-group>");
	}

	@Override
	public String getOutputFormat() {
		return "xml";
	}

	@Override
	public void initialize(IEmitterServices services) throws BirtException {
		//check the output stream
		outputStream = services.getRenderOption().getOutputStream();
		
		if (outputStream == null)
		{
			String outputFileName = services.getRenderOption().getOutputFileName();
			try {
				outputStream = new FileOutputStream(outputFileName);
			}
			catch (FileNotFoundException e) {
				throw new BirtException(e.getMessage());
			}
		}
	}

	@Override
	public void start(IReportContent reportContent) throws BirtException {
		stringBuilder = new StringBuilder();
		
		//add the XML header
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		stringBuilder.append("<report>");
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		//autotext is self contained
		stringBuilder.append("<auto-text");
		
		appendAttribute(autoText);
		
		stringBuilder.append(">");
		stringBuilder.append(autoText.getText());
		stringBuilder.append("</auto-text>");
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		stringBuilder.append("<cell");
		
		appendAttribute(cell);
		
		stringBuilder.append(">");
	}

	@Override
	public void startContainer(IContainerContent containerContent) throws BirtException {
		stringBuilder.append("<container");
		appendAttribute(containerContent);
		stringBuilder.append(">");
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		stringBuilder.append("<content");
		appendAttribute(content);
		stringBuilder.append(">");
	}

	@Override
	public void startData(IDataContent dataContent) throws BirtException {
		stringBuilder.append("<data");
		appendAttribute(dataContent);
		stringBuilder.append(">");
		stringBuilder.append(dataContent.getText());
		stringBuilder.append("</data>");
	}

	@Override
	/**
	 * As a report developer
	 * when I have a report with a dynamic text item
	 * then I need the XML format to display the result
	 * 
	 * Foreigns appear to be resulted from Dynamic Text Items
	 */
	public void startForeign(IForeignContent foriegn) throws BirtException {
		stringBuilder.append("<foriegn");
		appendAttribute(foriegn);	
		stringBuilder.append(">");
		
		String o = (String) foriegn.getRawValue();
		stringBuilder.append(o);
		stringBuilder.append("</foriegn>");
	}

	@Override
	public void startGroup(IGroupContent groupContent) throws BirtException {
		stringBuilder.append("<group");
		appendAttribute(groupContent);
		stringBuilder.append(">");
	}

	@Override
	public void startImage(IImageContent imageContent) throws BirtException {
		stringBuilder.append("<image");
		appendAttribute(imageContent);
		stringBuilder.append(">");
		
		//encode image data as Base64
		stringBuilder.append(Base64.encode(imageContent.getData()));
		stringBuilder.append("</image>");
	}

	@Override
	public void startLabel(ILabelContent label) throws BirtException {
		stringBuilder.append("<label");
		appendAttribute(label);
		stringBuilder.append(">");
		stringBuilder.append(label.getLabelText());
		stringBuilder.append("</label>");
	}

	@Override
	public void startList(IListContent listContent) throws BirtException {
		stringBuilder.append("<list");
		appendAttribute(listContent);
		stringBuilder.append(">");
	}

	@Override
	public void startListBand(IListBandContent listBandContent) throws BirtException {
		stringBuilder.append("<list-band");
		appendAttribute(listBandContent);
		stringBuilder.append(">");
	}

	@Override
	public void startListGroup(IListGroupContent listGroupContent) throws BirtException {
		stringBuilder.append("<list-group");
		appendAttribute(listGroupContent);
		stringBuilder.append(">");
	}

	@Override
	public void startPage(IPageContent pageContent) throws BirtException {
		stringBuilder.append("<page");
		appendAttribute(pageContent);
		stringBuilder.append(">");
	}

	@Override
	public void startRow(IRowContent rowContent) throws BirtException {
		stringBuilder.append("<row");
		appendAttribute(rowContent);
		stringBuilder.append(">");
	}

	@Override
	public void startTable(ITableContent tableContent) throws BirtException {
		stringBuilder.append("<table");
		appendAttribute(tableContent);
		stringBuilder.append(">");
	}

	@Override
	public void startTableBand(ITableBandContent tableBandContent) throws BirtException {
		stringBuilder.append("<table-band");
		appendAttribute(tableBandContent);
		String bandType = null;
		switch (tableBandContent.getBandType())
		{
			case ITableBandContent.BAND_DETAIL:
				bandType = "BAND_DETAIL";
				break;
			case ITableBandContent.BAND_FOOTER:
				bandType = "BAND_FOOTER";
				break;
			case ITableBandContent.BAND_HEADER:
				bandType = "BAND_HEADER";
				break;
			case ITableBandContent.BAND_GROUP_FOOTER:
				bandType = "BAND_GROUP_FOOTER";
				break;
			case ITableBandContent.BAND_GROUP_HEADER:
				bandType = "BAND_GROUP_HEADER";
				break;
		}
		
		if (bandType != null)
		{
			appendAttribute("band-type", bandType);
		}
		stringBuilder.append(">");
	}

	@Override
	public void startTableGroup(ITableGroupContent tableGroupContent) throws BirtException {
		stringBuilder.append("<table-group");
		appendAttribute(tableGroupContent);
		stringBuilder.append(">");
	}

	@Override
	public void startText(ITextContent textContent) throws BirtException {
		stringBuilder.append("<text");
		stringBuilder.append(">");
		stringBuilder.append(textContent.getText());
		stringBuilder.append("</text>");
	}
}
