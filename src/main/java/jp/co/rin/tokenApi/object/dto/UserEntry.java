package jp.co.rin.tokenApi.object.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserEntry {

	private String userId;
	private String password;
	private List<String> roles;
}

/*
 *  正規ユーザか検証後、権限属性も返す。
 *  見つからなかった場合の null 返却で不正ユーザ判定とする。
 *
 *  List　型の roles に、JWT の payload に入れる要素を詰め込む。
 */