'use strict';

import React, { useState, useEffect } from 'react';
import SimpleTable, { HeaderItem } from './SimpleTable';

const pageSize = 5;

type Itemlist = {
	total: number;
	items: any[];
}

const noItems: Itemlist = { total: 0, items: [] };

type PageArgs = {
	number: number;
	pageSize: number;
}

const firstPage: PageArgs = { number: 0, pageSize: pageSize };

type ItemState = {
	isLoaded: boolean;
	error: string | null;
	itemList: Itemlist;
}

const notLoaded: ItemState = { isLoaded: false, error: null, itemList: noItems }

type Props = {
	columns: HeaderItem[];
	onRow: (item: any) => void;
	load: (page: PageArgs, setItems: (items: Itemlist) => void, setError: (error: string) => void) => void
}

const PaginatedTable: React.FC<Props> = ({ columns, onRow, load }) => {
	const [itemState, setItemState] = useState<ItemState>(notLoaded);
	const [page, setPage] = useState<PageArgs>(firstPage);

	const loadData = () => {
		setItemState(notLoaded);
		load(
			page,
			itemList => setItemState({ isLoaded: true, error: null, itemList: itemList }),
			error => setItemState({ isLoaded: true, error: error, itemList: noItems }));
	};

	useEffect(() => {
		loadData();
	}, []);

	const prevPage = () => {
		const newPage = { number: page.number - 1, pageSize: pageSize };
		setPage(newPage);
	}
	const nextPage = () => {
		const newPage = { number: page.number + 1, pageSize: pageSize };
		setPage(newPage);
	}
	const lastPage = () => {
		const newPage = { number: Math.ceil(itemState.itemList.total / pageSize) - 1, pageSize: pageSize };
		setPage(newPage);
	}

	//TODO set first page and reload on enter in filter, reload on page buttons
	const first = <button disabled={page.number === 0} onClick={() => setPage(firstPage)}>First</button>
	const prev = <button disabled={page.number === 0} onClick={prevPage}>Previous</button>;
	const next = <button disabled={((page.number + 1) * pageSize) >= itemState.itemList.total} onClick={nextPage}>Next</button>;
	const last = <button disabled={((page.number + 1) * pageSize) >= itemState.itemList.total} onClick={lastPage}>Last</button>;
	const reload = <button onClick={loadData}>Reload</button>;
	return (<div>
		<p>Showing page {page.number + 1} of {itemState.itemList.total} items</p>
		{first}
		{prev}
		{next}
		{last}
		{reload}
		<SimpleTable columns={columns} items={itemState.itemList.items} isLoaded={itemState.isLoaded} error={itemState.error} onRow={onRow} />
	</div>);
}

export type { Itemlist, PageArgs };
export default PaginatedTable;