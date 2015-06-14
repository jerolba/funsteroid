package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import freemarker.ext.beans.StringModel;
import freemarker.log.Logger;
import freemarker.template.SimpleDate;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

@SuppressWarnings("rawtypes")
public class MacroHelper {

	private static final Logger logger = Logger.getLogger(MacroHelper.class.getCanonicalName());

	public static HashMap<String, Object> extractParams(Map params){
		HashMap<String, Object> res=new HashMap<String, Object>();

		Set keys=params.keySet();
		Iterator it=keys.iterator();
		while (it.hasNext()){
			String attrName=(String) it.next();
			Object obj=params.get(attrName);
			if (obj!=null){
				try{
					Object value=unwrapObject(obj);
					res.put(attrName, value);
				}catch(Exception e){
					logger.error("Parameter "+attrName+" of type "+obj.getClass().getCanonicalName()+" throw an exception",e);
					e.printStackTrace();
				}
			}else{
				logger.warn("Parameter "+attrName+" is null");
			}
		}
		return res;
	}

	public static <T> T get(Map params, String paramName, Class<T> class1) {
		Object param=params.get(paramName);
		if (param!=null && param instanceof StringModel){
			StringModel criteriaModel=(StringModel)param;
			Object obj=criteriaModel.getWrappedObject();
			if (class1.isAssignableFrom(obj.getClass())){
				return (T)obj;
			}
		}else if (param!=null && !(param instanceof TemplateSequenceModel)){
			logger.error("Macro param type invalid");
		}
		return null;
	}

	public static Object unwrapObject(Object obj){
		Object value=null;
		if (obj instanceof SimpleScalar){
			value =((SimpleScalar)obj).toString();
		} else if (obj instanceof StringModel){
			StringModel strm=(StringModel)obj;
			value =strm.getWrappedObject();
		}else if (obj instanceof SimpleNumber){
			SimpleNumber sn=(SimpleNumber) obj;
			value = sn.getAsNumber();
		}else if (obj instanceof SimpleDate){
			SimpleDate sn=(SimpleDate) obj;
			value=sn.getAsDate();
		}else if (obj instanceof SimpleSequence){
			SimpleSequence ss=(SimpleSequence) obj;
			value=getSequence(ss);
		}else{
			//TODO: Completar los tipos básicos que puedan llegar
			logger.error("Parameter of type "+obj.getClass().getCanonicalName()+" is not recognised");
		}
		return value;
	}

	public static Integer getInt(Map params, String paramName){
		SimpleNumber valueModel=(SimpleNumber)params.get(paramName);
		if (valueModel!=null){
			int value=valueModel.getAsNumber().intValue();
			return value;
		}
		return null;
	}

	public static Long getLong(Map params, String paramName){
		SimpleNumber valueModel=(SimpleNumber)params.get(paramName);
		if (valueModel!=null){
			Long value=valueModel.getAsNumber().longValue();
			return value;
		}
		return null;
	}

	public static String getString(Map params, String paramName){
		Object objParam=params.get(paramName);
		if (objParam instanceof SimpleScalar){
			SimpleScalar valueModel=(SimpleScalar) params.get(paramName);
			if (valueModel!=null){
				return valueModel.getAsString();
			}
		}
		return null;
	}

	public static BigDecimal getBigDecimal(Map params, String paramName){
		SimpleNumber valueModel=(SimpleNumber)params.get(paramName);
		if (valueModel!=null){
			if (valueModel.getAsNumber() instanceof BigDecimal){
				return (BigDecimal)valueModel.getAsNumber();
			}
			return new BigDecimal(valueModel.getAsNumber().doubleValue());
		}
		return null;
	}

	public static Date getDate(Map params, String paramName){
		SimpleDate valueModel=(SimpleDate)params.get(paramName);
		if (valueModel!=null){
			Date value=valueModel.getAsDate();
			return value;
		}
		return null;
	}

	public static List<Object> getSequence(SimpleSequence ss){
		List<Object> res=new ArrayList<Object>();
		for(int i=0;i<ss.size();i++){
			try {
				Object obj=ss.get(i);
				res.add(unwrapObject(obj));
			} catch (TemplateModelException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public static ByteArrayOutputStream renderInnerTag(TemplateDirectiveBody body) throws TemplateException, IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		if (body==null) return baos;
		Writer writer=new PrintWriter(baos);
		body.render(writer);
		writer.close();
		return baos;
	}
}
