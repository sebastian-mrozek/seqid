export type NumericSequenceDefinition = {
  namespace: string;
  name: string;
  start: number;
};

export type NumericSequence = {
  id: string;
  sequenceDefinition: NumericSequenceDefinition;
  lastValue: number;
};
