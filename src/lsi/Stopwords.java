package lsi;

import java.io.FileWriter;
import java.io.IOException;


public class Stopwords
{
  





    public void geraStopwords(String pesquisa)
    {
        String itensPesquisa[];
        itensPesquisa = pesquisa.split(" ");
        String stopwordsENJV[] = {"boolean","abstract","break","future","byte","class","case","generic","char","extends","continue","goto","double","implements","default","inner","float","import","do","native","int","instanceOf","else","operatorshort","interface","for","outer","long","super","if","package","this","new","rest","void","return","synchronized","switch","transient","while","var","volatile","false","catch","const","null","finally","final","true","throw","private","throws","protected","try","public","static","","a","about","above","above","across","after","afterwards","again","against","all","almost","alone","along","already","also","although","always","am","among","amongst","amoungst","amount","an","and","another","any","anyhow","anyone","anything","anyway","anywhere","are","around","as","at","back","be","became","because","become","becomes","becoming","been","before","beforehand","behind","being","below","beside","besides","between","beyond","bill","both","bottom","but","by","call","can","cannot","cant","co","con","could","couldnt","cry","de","describe","detail","do","done","down","due","during","each","eg","eight","either","eleven","else","elsewhere","empty","enough","etc","even","ever","every","everyone","everything","everywhere","except","few","fifteen","fify","fill","find","fire","first","five","for","former","formerly","forty","found","four","from","front","full","further","get","give","go","had","has","hasnt","have","he","hence","her","here","hereafter","hereby","herein","hereupon","hers","herself","him","himself","his","how","however","hundred","ie","if","in","inc","indeed","interest","into","is","it","its","itself","keep","last","latter","latterly","least","less","ltd","made","many","may","me","meanwhile","might","mill","mine","more","moreover","most","mostly","move","much","must","my","myself","name","namely","neither","never","nevertheless","next","nine","no","nobody","none","noone","nor","not","nothing","now","nowhere","of","off","often","on","once","one","only","onto","or","other","others","otherwise","our","ours","ourselves","out","over","own","part","per","perhaps","please","put","rather","re","same","see","seem","seemed","seeming","seems","serious","several","she","should","show","side","since","sincere","six","sixty","so","some","somehow","someone","something","sometime","sometimes","somewhere","still","such","system","take","ten","than","that","the","their","them","themselves","then","thence","there","thereafter","thereby","therefore","therein","thereupon","these","they","thickv","thin","third","this","those","though","three","through","throughout","thru","thus","to","together","too","top","toward","towards","twelve","twenty","two","un","under","until","up","upon","us","very","via","was","we","well","were","what","whatever","when","whence","whenever","where","whereafter","whereas","whereby","wherein","whereupon","wherever","whether","which","while","whither","who","whoever","whole","whom","whose","why","will","with","within","without","would","yet","you","your","yours","yourself","yourselves","the"};
        String stopwordsEN[]   = {"a","about","above","across","after","afterwards","again","against","all","almost","alone","along","already","also","although","always","am","among","amongst","amoungst","amount","an","and","another","any","anyhow","anyone","anything","anyway","anywhere","are","around","as","at","back","be","became","because","become","becomes","becoming","been","before","beforehand","behind","being","below","beside","besides","between","beyond","bill","both","bottom","but","by","call","can","cannot","cant","co","con","could","couldnt","cry","de","describe","detail","do","done","down","due","during","each","eg","eight","either","eleven","else","elsewhere","empty","enough","etc","even","ever","every","everyone","everything","everywhere","except","few","fifteen","fify","fill","find","fire","first","five","for","former","formerly","forty","found","four","from","front","full","further","get","give","go","had","has","hasnt","have","he","hence","her","here","hereafter","hereby","herein","hereupon","hers","herself","him","himself","his","how","however","hundred","I","ie","if","in","inc","indeed","interest","into","is","it","its","instead","itself","keep","last","latter","latterly","least","less","ltd","made","many","may","me","meanwhile","might","mill","mine","more","moreover","most","mostly","move","much","must","my","myself","name","namely","neither","never","nevertheless","next","nine","no","nobody","none","noone","nor","not","nothing","now","nowhere","of","off","often","on","once","one","only","onto","or","other","others","otherwise","our","ours","ourselves","out","over","own","part","per","perhaps","please","put","rather","re","same","see","seem","seemed","seeming","seems","serious","several","she","should","show","side","since","sincere","six","sixty","so","some","somehow","someone","something","sometime","sometimes","somewhere","still","such","system","take","ten","than","that","the","their","them","themselves","then","thence","there","thereafter","thereby","therefore","therein","thereupon","these","they","thickv","thin","third","this","those","though","three","through","throughout","thru","thus","to","together","too","top","toward","towards","twelve","twenty","two","un","under","until","up","upon","us","very","via","was","we","well","were","what","whatever","when","whence","whenever","where","whereafter","whereas","whereby","wherein","whereupon","wherever","whether","which","while","whither","who","whoever","whole","whom","whose","why","will","with","within","without","would","yet","you","your","yours","yourself","yourselves","the"};
        String stopwordsJV[]   = {"boolean","abstract","break","future","byte","class","case","generic","char","extends","continue","goto","double","implements","default","inner","float","import","do","native","int","instanceOf","else","operatorshort","interface","for","outer","long","super","if","package","this","new","rest","void","return","synchronized","switch","transient","while","var","volatile","false","catch","const","null","finally","final","true","throw","private","throws","protected","try","public","static"};
        String stopwords="";



        //Gerando dinamicamente as stopwords TODAS
        int flagIgual;
        for(int i=0;i<=stopwordsENJV.length-1;i++)
        {
            flagIgual=0;
            for(int k=0;k<=itensPesquisa.length-1;k++)
            {
                    if(stopwordsENJV[i].equalsIgnoreCase(itensPesquisa[k]))
                    {
                        flagIgual=1;
                    }
            }
            if(flagIgual==0)
                stopwords = stopwords + " " + stopwordsENJV[i];
        }

        String nomeArquivo = "c:\\analise\\stopwords\\stopwords_en+jv.txt";
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            fw.write(stopwords);
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }


        //Gerando dinamicamente as stopwords ENGLISH
        stopwords="";
        for(int i=0;i<=stopwordsEN.length-1;i++)
        {
            flagIgual=0;
            for(int k=0;k<=itensPesquisa.length-1;k++)
            {
                    if(stopwordsEN[i].equalsIgnoreCase(itensPesquisa[k]))
                    {
                        flagIgual=1;
                    }
            }
            if(flagIgual==0)
                stopwords = stopwords + " " +  stopwordsEN[i];
        }

        nomeArquivo = "c:\\analise\\stopwords\\stopwords_en.txt";
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            fw.write(stopwords);
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }





        //Gerando dinamicamente as stopwords JAVA
        stopwords="";
        for(int i=0;i<=stopwordsJV.length-1;i++)
        {
            flagIgual=0;
            for(int k=0;k<=itensPesquisa.length-1;k++)
            {
                    if(stopwordsJV[i].equalsIgnoreCase(itensPesquisa[k]))
                    {
                        flagIgual=1;
                    }
            }
            if(flagIgual==0)
                stopwords = stopwords + " " + stopwordsJV[i];
        }

        nomeArquivo = "c:\\analise\\stopwords\\stopwords_jv.txt";
        try
        {
            FileWriter fw = new FileWriter(nomeArquivo, false);
            fw.write(stopwords);
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        
   }
}
