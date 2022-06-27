export interface ILogiciel {
  id?: number;
  nomLogiciel?: string | null;
  description?: string | null;
}

export class Logiciel implements ILogiciel {
  constructor(public id?: number, public nomLogiciel?: string | null, public description?: string | null) {}
}

export function getLogicielIdentifier(logiciel: ILogiciel): number | undefined {
  return logiciel.id;
}
