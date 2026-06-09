create table interactions (
    id bigserial primary key,
    lead_id bigint not null,
    type varchar(40) not null,
    description varchar(255) not null,
    created_at timestamp not null,
    constraint fk_interactions_lead
        foreign key (lead_id)
        references leads (id)
);
