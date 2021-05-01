import axios from "axios";
import type { NumericSequence, NumericSequenceDefinition } from "./model";

interface SequenceService {
  getAll(onResolve: (data: NumericSequence[]) => void): void;
  create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void);
}

export const sequenceService: SequenceService = ((): SequenceService => {
  const sequenceService = axios.create({ baseURL: "http://localhost:7000/sequence" });

  function handleError(error) {
    console.log(error);
  }
  function log(response) {
    console.log(response);
  }

  function getAll(onResolve: (data: NumericSequence[]) => void) {
    sequenceService
      .get<NumericSequence[]>("")
      .then((response) => {
        log(response);
        onResolve(response.data);
      })
      .catch(handleError);
  }

  function create(newSequenceDefinition: NumericSequenceDefinition, onSuccess: (newSequence: NumericSequence) => void) {
    sequenceService
      .post("", newSequenceDefinition)
      .then((response) => {
        log(response);
        onSuccess(response.data);
      })
      .catch(handleError);
  }
  return {
    getAll,
    create,
  };
})();
