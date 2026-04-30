package jp.co.rin.tokenApi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.rin.tokenApi.object.dto.UserEntry;

@Mapper
public interface LoginRepository {

	public UserEntry searchUser(@Param("userId") String userId);

}
/*
 * Repositoru.xml 側で serchUser をMAPのidとしているので、メソッド名の一致要
 * [searchUser(String id)] 検索はメソッド引数の userId と　USER_KANRI テーブルの user_id の一致を要件とする
 *
 */
