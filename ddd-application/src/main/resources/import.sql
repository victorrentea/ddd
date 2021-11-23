// runs after hibernate schema generation.
select 1 from dual;

alter table backlog_item add constraint FK_ITEM_TO_PRODUCT foreign key (product_id) references product;