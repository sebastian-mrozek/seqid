<script lang="ts">
  import type { NumericSequenceDefinition } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  const empty = (): NumericSequenceDefinition => {
    return {
      name: "",
      namespace: "",
      start: 1,
      length: null,
      max: null,
      prefix: null,
      suffix: null,
    };
  };
  let definition: NumericSequenceDefinition = empty();

  function onClick() {
    dispatch("create", definition);
    definition = empty();
  }

  $: valid = definition.namespace.length > 0 && definition.name.length > 0 && definition.start !== undefined && definition.start !== null;
</script>

<div class="editor input-group fluid">
  <label for="name">Name</label>
  <input id="name" type="text" bind:value={definition.name} size="5" />
  <label for="namespace">Namespace</label>
  <input id="namespace" type="text" bind:value={definition.namespace} size="5" />
  <label for="start">Start</label>
  <input id="start" type="number" bind:value={definition.start} min="0" size="2" />
  <label for="length">Length</label>
  <input id="length" type="number" bind:value={definition.length} min="1" size="2" />
  <label for="prefix">Prefix</label>
  <input id="prefix" type="text" bind:value={definition.prefix} size="5" />
  <label for="suffix">Suffix</label>
  <input id="suffix" type="text" bind:value={definition.suffix} size="5" />
  <button on:click={onClick} disabled={!valid}>Create</button>
</div>

<style>
  .editor {
    margin-top: 1em;
    background-color: #eee;
    border: 1px solid #ddd;
    border-radius: 3px;
    padding: 0.5em;
  }
</style>
