package DiscordClownBot;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotMain extends ListenerAdapter
{
	static String[][] list = new String[5][4];
	static boolean[] checkArr;
	
    public static void main(String[] args)
            throws LoginException
    {
        JDA jda = JDABuilder.createDefault(System.getenv("token")).build();
        //You can also add event listeners to the already built JDA instance
        // Note that some events may not be received if the listener is added after calling build()
        // This includes events such as the ReadyEvent
        
        listInit();
        checkArrInit();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new BotMain());
    }

    private static void checkArrInit() {
		checkArr = new boolean[5];
	}

	private static void listInit() {
		list[0] = new String[] {"홀리GO", "나지금로아해", "종이박", "됴으닝"};
		list[1] = new String[] {"LOAON", "낭만창술사라라", "괄파이터", "오레하제빵소홀서빙"};
		list[2] = new String[] {"MUNTAE", "Jerlcho", "펩시롯데리아부먹D점멸", "괄리단"};
		list[3] = new String[] {"아옹입니닷", "4번째마법사", "좀군", "나지금분뇨해"};
		list[4] = new String[] {"Go화가", "괄도넴", "코즐로브나", "아임아이번맨"};
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!list"))
        {
        	List<Field> fieldList = new LinkedList<>();
            for (int i = 0; i < list.length; i++) {
            	String text = "";
            	for (int j = 0; j < list[i].length - 1; j++) {
            		text += "`" + list[i][j] + "`, ";
				}
            	text += "`" + list[i][list[i].length - 1] + "`";
            	Field field = new Field((i+1) + "." + " " + (checkArr[i]? ":ballot_box_with_check:" : ""), text, false);
            	fieldList.add(field);
			}
            MessageChannel channel = event.getChannel();
            MessageEmbed embed = new MessageEmbed(null, "쿠크세이튼 파티 목록", "에스더의 기운을 먹는 그 날까지..", null, null, 0xff0000, null, null, null, null, null, null, fieldList);
            channel.sendMessage(embed).queue();
        }
        
        if (msg.getContentRaw().startsWith("!check")) 
        {
        	String str = msg.getContentRaw();
        	StringTokenizer st = new StringTokenizer(str);
        	int cntToken = st.countTokens();
        	st.nextToken();
        	String name = st.nextToken();
        	int partyNum = findNameInList(name);
        	if(cntToken == 2 && partyNum != -1) {
        		MessageChannel channel = event.getChannel();
        		if(checkArr[partyNum]) {
        			checkArr[partyNum] = false;
        			channel.sendMessage((partyNum + 1) + "파티는 쿠크세이튼을 안 간 것으로 바꿔드렸습니다.").queue(message -> {
        				message.addReaction("U+1F602").queue();
        			});
        		}
        		else {
        			checkArr[partyNum] = true;
        			channel.sendMessage((partyNum + 1) + "번 파티는 이번 주 쿠크세이튼을 다녀왔습니다. 에스더의 기운은요?").queue(message -> {
        				message.addReaction("U+1F602").queue();
        			});
        		}
        		List<Field> fieldList = new LinkedList<>();
                for (int i = 0; i < list.length; i++) {
                	String text = "";
                	for (int j = 0; j < list[i].length - 1; j++) {
                		text += "`" + list[i][j] + "`, ";
    				}
                	text += "`" + list[i][list[i].length - 1] + "`";
                	Field field = new Field((i+1) + "." + " " + (checkArr[i]? ":ballot_box_with_check:" : ""), text, false);
                	fieldList.add(field);
    			}
                MessageEmbed embed = new MessageEmbed(null, "쿠크세이튼 파티 목록", "에스더의 기운을 먹는 그 날까지..", null, null, 0xff0000, null, null, null, null, null, null, fieldList);
                channel.sendMessage(embed).queue();
        	}
        }
        
        if (msg.getContentRaw().equals("!init")) {
        	checkArrInit();
        	MessageChannel channel = event.getChannel();
    		channel.sendMessage("파티 일정을 초기화합니다.").queue();
        }
    }

	private int findNameInList(String name) {
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < list[i].length; j++) {
				if(list[i][j].equals(name)) {
					return i;
				}
			}
		}
		return -1;
	}
}