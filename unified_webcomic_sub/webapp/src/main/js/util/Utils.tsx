
type ArrayFunc = (inVal: any[]) => any[];

const compact: ArrayFunc = (inVal) => {
	return Object.values(inVal);
	//return Object.keys(inVal).map(i => inVal[i]);
}

const containsAny: (inVal: any[]) => boolean = (inVal) => {
	return compact(inVal).length === 0
}

export {
	compact,
	containsAny,
}