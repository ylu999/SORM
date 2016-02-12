# SORM
a simple light weight SORM framework, originally learned from a tutorial https://www.youtube.com/playlist?list=PLTstZD3AK3S-eVO1jay1EURUVHG20_5dq

http://gvace.blogspot.com/2016/02/sorm.html


1. **IMPORTANT: To generate PO sources, TableContext.updatePOFiles() will be manually called by developer themselves. 
2. Function loadPOTables will be called by DBManager in static block.
3. We support one and only one primary key for each table.
4. Try your best to use boxing objects instead of primitive types.
