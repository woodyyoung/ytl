    
     --获取一条线路的站点数据
     insert into pjmk_platform_station 
     SELECT t.PLATFORM_ID,
            t.name,
            t.id,
            decode(t.rn, 1, 0, 1) dr,
            t.longitude,
            t.latitude,
            t.gprsid,
            t.orderno
            
       FROM (SELECT t.*,
                    ROW_NUMBER() OVER(PARTITION BY t.name ORDER BY t.gprsid) AS RN
               FROM pjmk_station t) t
      WHERE RN <= 2;
     
     --获取其他线路站点数据
     insert into pjmk_platform_station 
     select t.platform_id,
            t.name,
            t.id,
            decode(sign(t.ldis - rdis), -1, 0, 0, 0, 1) dds,
            t.longitude,
            t.latitude,
            t.gprsid,
            t.orderno
       from (select GetDistance(t.LONGITUDE,
                                t.LATITUDE,
                                l.LONGITUDE,
                                l.LATITUDE) ldis,
                    GetDistance(t.LONGITUDE,
                                t.LATITUDE,
                                r.LONGITUDE,
                                r.LATITUDE) rdis,
                    t.*
               from （
                     select t.*
                       from pjmk_station t
                      where t.id not in
                            (select t.STATION_ID from pjmk_platform_station t) ） t,
                     
                      pjmk_platform_station l, pjmk_platform_station r
                     
                      where t.platform_id = l.platform_id
                        and t.platform_id = r.platform_id
                        and l.direction = 0
                        and r.direction = 1
             ) t;
              
             
 		--线路起点站、终点点
        create or replace view v_line_start_end_station as

		select t.*, l.name lname
		  from pjmk_line l,
		       (select t1.gprsid,
		               t1.direction,
		               t2.orderno   firsorderno,
		               t2.firsName,
		               t1.orderno   lasorderno,
		               t1.lasName
		          from (select t.gprsid, t.direction, t.orderno, t.name lasName
		                  from pjmk_station t,
		                       (select t.gprsid, t.direction, max(t.orderno) orderno
		                          from pjmk_station t
		                         group by t.gprsid, t.direction) las
		                 where t.gprsid = las.gprsid
		                   and t.direction = las.direction
		                   and t.orderno = las.orderno) t1,
		               (select t.gprsid, t.direction, t.orderno, t.name firsName
		                  from pjmk_station t,
		                       
		                       (select t.gprsid, t.direction, min(t.orderno) orderno
		                          from pjmk_station t
		                         group by t.gprsid, t.direction) firs
		                
		                 where t.gprsid = firs.gprsid
		                   and t.direction = firs.direction
		                   and t.orderno = firs.orderno) t2
		         where t1.gprsid = t2.gprsid
		           and t1.direction = t2.direction) t
		
		 where t.gprsid = l.gprsid;
		 
		 --创建站台客流视图
		create view V_PLATFORM_PSGFLOW as 
select p.platform_name p_name,
       f.platform_id,
       p.longitude,
       p.latitude,
       p.direction,
       to_char(f.occur_time, 'yyyy-mm-dd') dd,
       to_char(f.occur_time, 'dd') d,
       to_char(f.occur_time, 'yyyy-mm') mm,
       to_char(f.occur_time, 'HH24') hh,
       to_char(f.occur_time, 'yyyy-iw') iw,
       f.total_person_count,
       f.onbus_person_count,
       f.offbus_person_count,
       f.occur_time,
       f.holiday_flag,
       f.line_id
  from Pjmk_Station_Psgflow f, pjmk_platform_station p
 where f.station_id = p.station_id;
 
 
 ---执行指标数据统计
    declare
     days date;
   
   begin
     days := sysdate - 100;
     while days < sysdate - 15 loop
       dbms_output.put_line(to_char(days, 'yyyy-mm-dd'));
       proc_avalsys_evaluate_entrance(days);
       days := days + 1;
     end loop;
   end;

   
   
   
   ---2017-06-22
   
 
create or replace view v_total_level as
select a.id,a.name ,a.level_unit,a.weight,a.targetlevel , b.count_date,b.actual_score, b.actual_level,a.parentId,b.is_work,
  to_char(b.COUNT_DATE,'yyyy-mm-dd') dd,
  to_char(b.COUNT_DATE,'yyyy-mm') mm,
  to_char(b.COUNT_DATE,'yyyy-iw') iw,
  to_char(b.COUNT_DATE,'yyyy') yea
 from pjmk_index a,
 v_index_source_data_level b where a.ID = b.index_id;
 
 
create or replace view v_index_source_data as
select
    a.ID, a.INDEX_ID, a.INDEX_NUM, a.INDEX_TOTAL_NUM, a.COUNT_DATE, a.CREATETIME, a.UPDATETIME, a.ACTUAL_LEVEL, a.ACTUAL_SCORE,
     b.name,a.is_work,a.line_id,a.area_id,b.level_unit,
     to_char(a.COUNT_DATE,'yyyy-mm-dd') dd,
     to_char(a.COUNT_DATE,'yyyy-mm') mm,
     to_char(a.COUNT_DATE,'yyyy-iw') iw,
     to_char(a.COUNT_DATE,'yyyy') yea
    from v_index_source_data_level a left join pjmk_index b on a.index_id = b.id
    where 1=1;