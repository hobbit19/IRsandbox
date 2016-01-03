import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hakansahin on 03/01/16.
 */
public class Question{

    private int id;
    private String title, body;
    private Question[] relatedQuestions;
    private List<String> titleTokens, bodyTokens;

    public Question(String json){

        JSONObject jsonObject = (new JSONArray(json)).getJSONObject(0);
        init(jsonObject);

        JSONArray relatedQuestions = jsonObject.optJSONArray("related_questions");
        this.relatedQuestions = new Question[relatedQuestions.length()];
        for (int i = 0;i < this.relatedQuestions.length;i++) {
            JSONObject relatedQuestion = relatedQuestions.getJSONObject(i);
            this.relatedQuestions[i] = new Question(relatedQuestion);
        }
    }

    public Question(JSONObject jsonObject) {
        init(jsonObject);
    }

    public void init(JSONObject json){
        this.id = json.getInt("id");
        this.title = json.getString("title");
        this.body = json.getString("body");

        tokenize();
    }

    public void tokenize(){
        this.titleTokens = new ArrayList<String>();
        this.bodyTokens = new ArrayList<String>();
        PTBTokenizer<CoreLabel> tokenizer;

        tokenizer = new PTBTokenizer<CoreLabel>
                            (new StringReader(this.title), new CoreLabelTokenFactory(), "");
        while(tokenizer.hasNext())
            titleTokens.add(tokenizer.next().current());

        tokenizer = new PTBTokenizer<CoreLabel>
                            (new StringReader(this.body), new CoreLabelTokenFactory(), "");
        while(tokenizer.hasNext())
            bodyTokens.add(tokenizer.next().current());
    }

    public int getID(){ return this.id; }
    public String getTitle(){ return this.title; }
    public String getBody(){ return this.body; }
    public Question[] getRelatedQuestions(){ return this.relatedQuestions; }
    public List<String> getTitleTokens(){ return this.titleTokens; }
    public List<String> getBodyTokens(){ return this.bodyTokens; }

}
