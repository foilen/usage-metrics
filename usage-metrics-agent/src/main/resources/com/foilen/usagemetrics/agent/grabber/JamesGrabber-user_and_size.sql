SELECT jmb.USER_NAME, SUM(jm.MAIL_CONTENT_OCTETS_COUNT) 
FROM `JAMES_MAIL` jm 
INNER JOIN `JAMES_MAILBOX` jmb ON jm.MAILBOX_ID = jmb.MAILBOX_ID 
GROUP BY jmb.USER_NAME