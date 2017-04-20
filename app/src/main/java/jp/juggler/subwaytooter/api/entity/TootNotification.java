package jp.juggler.subwaytooter.api.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.juggler.subwaytooter.util.LogCategory;
import jp.juggler.subwaytooter.util.Utils;

public class TootNotification {
	
	//	The notification ID
	public long id;
	
	//	One of: "mention", "reblog", "favourite", "follow"
	public String type;
	
	public static final String TYPE_MENTION = "mention";
	public static final String TYPE_REBLOG = "reblog";
	public static final String TYPE_FAVOURITE = "favourite";
	public static final String TYPE_FOLLOW = "follow";
	
	//	The time the notification was created
	public String created_at;
	
	//	The Account sending the notification to the user
	public TootAccount account;
	
	//	The Status associated with the notification, if applicable
	public TootStatus status;
	
	public long time_created_at;
	
	public static TootNotification parse( LogCategory log, JSONObject src ){
		if( src == null ) return null;
		try{
			TootNotification dst = new TootNotification();
			dst.id = src.optLong( "id" );
			dst.type = Utils.optStringX( src, "type" );
			dst.created_at = Utils.optStringX( src, "created_at" );
			dst.account = TootAccount.parse( log, src.optJSONObject( "account" ) );
			dst.status = TootStatus.parse( log, src.optJSONObject( "status" ) );
			
			
			dst.time_created_at = TootStatus.parseTime( log, dst.created_at );
			
			
			return dst;
		}catch( Throwable ex ){
			ex.printStackTrace();
			log.e( ex, "TootNotification.parse failed." );
			return null;
		}
	}
	

	public static class List extends ArrayList< TootNotification > {
		
	}
	
	public static List parseList( LogCategory log, JSONArray array ){
		List result = new List();
		if( array != null ){
			for( int i = array.length() - 1 ; i >= 0 ; -- i ){
				JSONObject src = array.optJSONObject( i );
				if( src == null ) continue;
				TootNotification item = parse( log, src );
				if( item != null ) result.add( 0, item );
			}
		}
		return result;
	}
}
