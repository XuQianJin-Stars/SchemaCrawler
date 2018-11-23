SELECT
  'SERVICE_NAME' AS NAME,
  SERVICE_NAME AS VALUE,
  '' AS DESCRIPTION
FROM
  V$SESSION
WHERE
  SID IN (SELECT DISTINCT SID FROM V$MYSTAT)
UNION ALL
SELECT
  'SID' AS NAME,
  INSTANCE AS VALUE,
  '' AS DESCRIPTION
FROM
  V$THREAD
