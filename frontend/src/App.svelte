<script lang="ts">
  import SequenceListView from "./components/SequenceListView.svelte";
  import type { NumericSequence } from "./model";

  let sequences: NumericSequence[] = [
    {
      id: "aa",
      sequenceDefinition: {
        name: "some sequence",
        namespace: "ns1",
        start: 1,
      },
      lastValue: 5,
    },
    {
      id: "ab",
      sequenceDefinition: {
        name: "another sequence",
        namespace: "ns2",
        start: 2,
      },
      lastValue: 55,
    },
  ];
</script>

<main>
  <SequenceListView {sequences} />
</main>

<style>
</style>
