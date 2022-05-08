'use strict';

import React, { useState } from 'react';
import InputBox from '../InputBox';
import PaginatedTable, { Itemlist, PageArgs } from '../table/PaginatedTable';
import { asyncFetchGetState } from '../../api/apiCall';
import { USER_SERVICE_MODERATORS_READ } from '../../api/apiEndpoints';
import { useNavigate } from 'react-router-dom';
import { UWSUser } from '../../api/entities';

type SearchArgs = {
	id?: string;
	name?: string;
};

const ModeratorsList: React.FC = () => {
	const navigate = useNavigate();
	const [search, setSearch] = useState<SearchArgs>({});

	const onId = (id: string) => {
		const newSearch = Object.assign({}, search);
		if (id === "") {
			delete newSearch.id;
		} else {
			newSearch.id = id;
		}
		setSearch(newSearch);
	};

	const onName = (name: string) => {
		const newSearch = Object.assign({}, search);
		if (name === "") {
			delete newSearch.name;
		} else {
			newSearch.name = name;
		}
		setSearch(newSearch);
	};

	const cols = [
		{
			key: "id",
			field: (item:UWSUser)=>item.id,
			content: <div><p>ID</p><InputBox initialValue="" setValue={onId} className="idcolumn" /></div>
		},
		{
			key: "name",
			field: (item:UWSUser)=>item.name,
			content: <div><p>Name</p><InputBox initialValue="" setValue={onName} /></div>
		}
	]

	const loadData = (page: PageArgs, setModerators: (itemList:Itemlist)=>void, setError:(error:string)=>void, )=>{
		const data:any = Object.assign({
			limit:page.pageSize,
			offset: page.number * page.pageSize,
		}, search);
		asyncFetchGetState(USER_SERVICE_MODERATORS_READ, data, setModerators, setError)
	}

	const onRow=(mod:UWSUser)=>{
		navigate("/mods/show/" + mod.id);
	}

	return (<PaginatedTable columns={cols} load={loadData} onRow={onRow}/>);
}

export default ModeratorsList;