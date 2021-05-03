<script lang="ts">
  import SequenceListView from "./components/SequenceListView.svelte";
  import NewSequenceEditor from "./components/NewSequenceEditor.svelte";
  import type { NumericSequence, NumericSequenceDefinition } from "./model";
  import { sequenceService } from "./service";

  let sequences: NumericSequence[] = [];

  function fetchSequences() {
    sequenceService.getAll((data) => (sequences = data));
  }

  function createNew(event: CustomEvent<NumericSequenceDefinition>) {
    sequenceService.create(event.detail, (newSequence) => (sequences = [...sequences, newSequence]));
  }

  function increment(event: CustomEvent<string>) {
    const id = event.detail;
    sequenceService.increment(id, (nextValue) => {
      const index = findIndex(sequences, id);
      sequences[index].lastValue = nextValue;
    });
  }

  function findIndex(sequences: NumericSequence[], id: string): number {
    let index = -1;
    sequences.forEach((s, i) => {
      if (s.id === id) index = i;
    });
    return index;
  }

  function reset(event: CustomEvent<string>) {
    const id = event.detail;
    sequenceService.reset(id, (updatedSequence) => {
      const index = findIndex(sequences, id);
      sequences[index] = updatedSequence;
    });
  }

  function remove(event: CustomEvent<string>) {
    const id = event.detail;
    sequenceService.remove(id, () => {
      const index = findIndex(sequences, id);
      sequences = sequences.filter((e, i) => i !== index);
    });
  }

  fetchSequences();
</script>

<main class="main">
  <div class="header">
    <h2 class="title">Seqid</h2>
    <NewSequenceEditor on:create={createNew} />
    <button class="refresh rounded" on:click={fetchSequences}>Refresh</button>
  </div>
  <SequenceListView {sequences} on:increment={increment} on:reset={reset} on:remove={remove} />
</main>

<style>
  .refresh {
    margin-left: auto;
  }
  .header {
    display: flex;
    margin: 1em;
    flex-wrap: wrap;
  }
  .title {
    margin-right: 2em;
  }
</style>
