
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