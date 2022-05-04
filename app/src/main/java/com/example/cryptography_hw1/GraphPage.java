package com.example.cryptography_hw1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
public class GraphPage extends AppCompatActivity {
    private Intent intent;
    private String Number;
    private static TextView PickNumber;
    private TextView Confidence,PopUpMsg;
    private Button Reset, ChangeGraph, Start;
    private RelativeLayout relativeLayoutLines,relativeLayoutNodes;
    private ImageView BackIcon;
    private int pick=-1,adj1=-1,adj2=-1,adj3=-1,color1,color2,color3;
    private double Conf = 0;
    private NodeView Nodes[];
    private boolean start = false;
    private ArrayList<ArrayList<Integer>> graph;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_page);
        PickNumber = findViewById(R.id.PickNumber);
        Reset = findViewById(R.id.Reset);
        ChangeGraph = findViewById(R.id.ChangeGraph);
        Start = findViewById(R.id.Start);
        PopUpMsg = findViewById(R.id.PopUpMsg);
        Confidence = findViewById(R.id.Confidence);
        BackIcon = findViewById(R.id.BackIcon);
        relativeLayoutLines = findViewById(R.id.relativeLayoutLines);
        relativeLayoutNodes = findViewById(R.id.relativeLayoutNodes);
        intent = getIntent();
        Number = intent.getStringExtra("NodeNumber");
        PickNumber.setText(Number + " Nodes");
        BackIcon();
        ResetButton();
        MakeGraph();
        StartButton();
        ChangeGraphButton();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void MakeGraph(){
        init();
        makeNodes();
        makeLines();
    }
    public void init(){
        Nodes = new NodeView[Integer.parseInt(Number)];
        graph = new ArrayList<ArrayList<Integer>>();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makeNodes(){
        Random random = new Random();
        for(int i=0;i < Integer.parseInt(Number);i++){
            int x=0,y=0;
            Boolean check = false;
            do{
                x = random.nextInt(1200);
                y = random.nextInt(1200);
                for(int j=0;j<Nodes.length;j++){
                    if(Nodes[j]!=null){
                    if(Math.abs(Nodes[j].getX() - x) <30 && Math.abs(Nodes[j].getY() - y) <30)
                        check = true;

                    }
                }
            }while (check == true);
            Nodes[i] = new NodeView(ChangeGraph.getContext(),i,x,y);
            relativeLayoutNodes.addView(Nodes[i]);
        }
    }
    public void makeLines(){
        Random random = new Random();
        int temp;
        setColors();
        for(int i=0;i < Integer.parseInt(Number); i++){
            graph.add(new ArrayList<>());
            for(int j=0; j<3; j++){
                do {
                    do { temp = random.nextInt(Integer.parseInt(Number));
                    } while (temp == i || (temp%3) == i%3);
                }while (graph.get(i).contains(temp));
                DrawView drawView = new DrawView(relativeLayoutLines.getContext(),Nodes[i],Nodes[temp],i,temp);
                Nodes[i].lines.add(drawView);
                Nodes[i].adj.add(temp);
                graph.get(i).add(temp);
                relativeLayoutLines.addView(Nodes[i].lines.get(j));
            }
        }
        for(int i=0;i < Integer.parseInt(Number); i++){
            for(int j=0; j<graph.get(i).size(); j++){
                if(!graph.get(graph.get(i).get(j)).contains(i)) {
                    graph.get(graph.get(i).get(j)).add(i);
                    Nodes[graph.get(i).get(j)].adj.add(i);
                    Nodes[graph.get(i).get(j)].lines.add(new DrawView(this,Nodes[i].lines.get(j).v2,Nodes[i].lines.get(j).v1,Nodes[i].lines.get(j).p2,Nodes[i].lines.get(j).p1));
                }
            }
        }
        PickNode();
    }
    public void setColors(){
        Random random = new Random();
        color1 = Color.argb(255, 255, 0, 0);  //red
        color2 = Color.argb(255, 0, 255, 0);  //green
        color3 = Color.argb(255, 0, 0, 255);  //blue
        int colors[] = {color1,color2,color3},temp;
        temp = random.nextInt(colors.length);
        for(int i=0 ;i<Nodes.length;i++)
                Nodes[i].color = colors[(temp+i)%3];
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ResetAll(){
        Nodes[pick].getBackground().setTint(getResources().getColor(R.color.black));
        Nodes[adj1].getBackground().setTint(getResources().getColor(R.color.black));
        Nodes[adj2].getBackground().setTint(getResources().getColor(R.color.black));
        Nodes[adj3].getBackground().setTint(getResources().getColor(R.color.black));
        pick =-1;adj1=-1;adj2=-1;adj3=-1;
        PopUpMsg.setText("Proofer: Please Pick Node ");
        Conf+= (float)(Integer.parseInt(Number) - 1) / Integer.parseInt(Number);
        if(Conf>=100)
            Confidence.setText("Confidence: 100%");
        else
            Confidence.setText("Confidence: " + String.format("%.3f", Conf) + "%");
    }
    public void PickNode(){
        PopUpMsg.setText("Proofer: Please Pick Node ");
        for(int i=0;i < Integer.parseInt(Number);i++) {
            Nodes[i].setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(pick==-1){
                        pick = v.getId();
                        Nodes[v.getId()].getBackground().setTint(Nodes[v.getId()].color);
                        PopUpMsg.setText("Proofer: Please Pick adjacent Number "+1);
                    }
                    if(pick != v.getId() && adj1==-1){
                        if(Nodes[pick].adj.contains(v.getId())) {
                            PopUpMsg.setText("Proofer: Please Pick adjacent Number "+2);
                            adj1 = v.getId();
                            Nodes[v.getId()].getBackground().setTint(Nodes[v.getId()].color);
                        }
                    }
                    if(pick != v.getId() && v.getId()!= adj1 && adj2==-1){
                        if(Nodes[pick].adj.contains(v.getId())) {
                            PopUpMsg.setText("Proofer: Please Pick adjacent Number "+3);
                            adj2 = v.getId();
                            Nodes[v.getId()].getBackground().setTint(Nodes[v.getId()].color);
                        }
                    }
                    if(pick != v.getId() && v.getId()!= adj1 && v.getId()!= adj2 && adj3==-1){
                        if(Nodes[pick].adj.contains(v.getId())) {
                            adj3 = v.getId();
                            Nodes[v.getId()].getBackground().setTint(Nodes[v.getId()].color);
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try { Thread.sleep(1000);
                                    } catch (InterruptedException e) { e.printStackTrace(); }
                                    ResetAll();
                                    setColors();
                                }
                            }.start();
                        }
                    }
                    return false;
                }
            });
        }
    }
    public void StartButton(){
        Start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                start = !start;
                if(Conf < 100) {
                    if (start) {
                        Start.setText("Stop");
                        for(int i=0;i<Nodes.length;i++)
                            Nodes[i].getBackground().setTint(Nodes[i].color);
                    }
                    else {
                        for(int i=0;i<Nodes.length;i++)
                            Nodes[i].getBackground().setTint(getResources().getColor(R.color.black));
                        Start.setText("Colors");
                    }
                }
                else
                    Start.setText("Finish");
            }
        });
    }
    public void ChangeGraphButton(){
        ChangeGraph.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                for(int i=0;i < Nodes.length;i++)
                    Nodes[i].ChangeCords();
            }
        });
    }
    public void ResetButton(){
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(GraphPage.this, MainActivity.class)); }
        });
    }
    public void BackIcon(){
        BackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(GraphPage.this, MainActivity.class)); }
        });
    }
    public class NodeView extends View {
        private ArrayList<DrawView> lines;
        private ArrayList<Integer> adj;
        private int color = getResources().getColor(R.color.black);
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public NodeView(Context context, int id , int x, int y) {
            super(context);

            lines = new ArrayList<>();
            adj = new ArrayList<>();
            super.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
            super.setId(id);
            super.setBackground(getResources().getDrawable(R.drawable.circle));
            super.getBackground().setTint(getResources().getColor(R.color.black));
            super.setX(x);
            super.setY(y);
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void ChangeCords(){
            Random random = new Random();
            super.setX(random.nextInt(1200));
            super.setY(random.nextInt(1200));
            for (int j = 0; j < lines.size(); j++)
                lines.get(j).Black();
        }
    }
    public class DrawView extends View {
        String id ="";
        private Paint paint = new Paint();
        private int color;
        private Canvas canvas;
        View v1;
        View v2;
        int p1;
        int p2;
        private float dashWidth = 15f;
        public DrawView(Context context) {
            super(context);
        }
        public DrawView(Context context,View v1,View v2,int p1,int p2) {
            super(context);
            this.v1 = v1;
            this.v2 = v2;
            id = p1+"-"+p2;
            this.p1 = p1;
            this.p2 = p2;
            paint.setColor(0xFF000000);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dashWidth);
        }
        @Override
        public void onDraw(Canvas canvas) {
            this.canvas = canvas;
            canvas.drawLine(Nodes[p1].getX()+52,Nodes[p1].getY()+50,Nodes[p2].getX()+52,Nodes[p2].getY()+50, paint);
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void Green(){
            paint.setColor(getResources().getColor(R.color.green));
            invalidate();
        }
        public void Black(){
            color = R.color.black;
            paint.setColor(getResources().getColor(color));
            invalidate();
        }
    }
}