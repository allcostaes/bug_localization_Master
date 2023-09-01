package arquivos;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;
import java.util.ArrayList;



public class Arquivo_Java1
{
	private String Nome_Completo;

        public ArrayList<String> pacoteClasseMetodo;

	Arquivo_Java1()
	{
            pacoteClasseMetodo = new ArrayList<String>();
        }

	public void setNome_Completo(String Nome_Completo)
	{
		this.Nome_Completo = Nome_Completo;
	}

	private void gravaMetodos(JavaClass aClass)
	{

	    JavaMethod methods[] = aClass.getMethods();

	    for (JavaMethod method : methods)
	    {

	    	String classePacote [] = (aClass.getFullyQualifiedName().toString()).split("\\.");
	    	String Classe = classePacote[(classePacote.length)-1];
                pacoteClasseMetodo.add(aClass.getPackageName() + ";" +  Classe + ";" + method.getName());

            }
	}

	public void listaClasses()
	{
            String aFileName=this.Nome_Completo;
            try
            {
                JavaDocBuilder builder = new JavaDocBuilder();
                builder.addSource(new FileReader(aFileName));
                JavaClass classes[] = builder.getClasses();
                for (JavaClass Classes_Java : classes)
                {
                    gravaMetodos(Classes_Java);
                }
            }catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
}
